package com.buffalosoftware.processengine.deployment;

import com.buffalosoftware.api.processengine.DefinitionManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class ProcessDeploymentManager {
    private Logger LOGGER = LoggerFactory.getLogger(ProcessDeploymentManager.class);

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;

    @Value("${spring.jpa.hibernate.ddlAuto:}")
    private String databaseInitState;

    @Autowired
    public void deployDefinitions(List<DefinitionManager> definitionManagers) {
        if(StringUtils.containsIgnoreCase(databaseInitState, "create")) {
            cleanUpInstancesAndDefinitions();
        }
        definitionManagers.stream()
                .filter(definitionManager -> !isProcessDeployed(definitionManager.getDefinitionKey()))
                .forEach(definitionManager -> {
                    BpmnModelInstance modelInstance = definitionManager.createModelInstance();
                    deploy(modelInstance, definitionManager.getDefinitionKey());
                });
    }

    private void deploy(BpmnModelInstance modelInstance, String processDefinitionKey) {
        Bpmn.validateModel(modelInstance);
        repositoryService.createDeployment()
                .name(processDefinitionKey)
                .addModelInstance(processDefinitionKey, modelInstance)
                .deploy();
        LOGGER.info("Process definition [{}] deployed", processDefinitionKey);
    }

    private boolean isProcessDeployed(String processDefinitionKey) {
        return isNotEmpty(repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).list());
    }

    private void cleanUpInstancesAndDefinitions() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        if (isNotEmpty(processDefinitions)) {
            processDefinitions.forEach(definition -> {
                List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                        .processDefinitionId(definition.getId())
                        .list();
                instances.forEach(i -> runtimeService.deleteProcessInstance(i.getProcessInstanceId(), "Cleaning up", true));
                repositoryService.deleteProcessDefinition(definition.getId());
                repositoryService.deleteDeployment(definition.getDeploymentId());
            });
        }
    }
}

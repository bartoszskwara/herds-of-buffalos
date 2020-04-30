package com.buffalosoftware.processengine.deployment;

import com.buffalosoftware.api.processengine.DefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class ProcessDeploymentManager {
    private Logger LOGGER = LoggerFactory.getLogger(ProcessDeploymentManager.class);

    private final RepositoryService repositoryService;

    @Autowired
    public void deployDefinitions(List<DefinitionManager> definitionManagers) {
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

    private void cleanUpDefinitions(String processDefinitionKey) {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        if (isNotEmpty(processDefinitions)) {
            processDefinitions.forEach(definition -> {
                repositoryService.deleteProcessDefinition(definition.getId());
                repositoryService.deleteDeployment(definition.getDeploymentId());
            });
        }
    }
}

package com.buffalosoftware.processengine.production.delegate;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.resource.IResourceService;
import com.buffalosoftware.entity.Resource;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_AMOUNT;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_NAME;

@Service
@RequiredArgsConstructor
public class ProduceResourcesTask implements JavaDelegate {

    Logger LOGGER = LoggerFactory.getLogger(ProduceResourcesTask.class);
    private final IProcessInstanceVariableProvider variableProvider;
    private final IResourceService resourceService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var amountToProduce = variableProvider.getVariable(delegateExecution, RESOURCE_PRODUCTION_AMOUNT, Integer.class);
        var resource = variableProvider.getVariable(delegateExecution, RESOURCE_PRODUCTION_NAME, String.class);
        var cityId = variableProvider.getVariable(delegateExecution, CITY_ID, Long.class);

        resourceService.increaseResources(cityId, Resource.getByName(resource), amountToProduce);

        LOGGER.debug("Resource [{}] produced in amount of [{}]", resource, amountToProduce);
    }
}

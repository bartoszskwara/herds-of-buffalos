package com.buffalosoftware.event;

import com.buffalosoftware.api.event.BuildingEvent;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.processengine.instance.ProcessInstanceProducerProvider;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.BUILDING_CONSTRUCTION_TIME;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_AMOUNT;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_NAME;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_TIME;
import static com.buffalosoftware.entity.Resource.BASE_PRODUCTION_AMOUNT;

@Service
@RequiredArgsConstructor
public class BuildingEventListener {

    private final Logger LOGGER = LoggerFactory.getLogger(BuildingEventListener.class);
    private final String BUILDING_UPGRADED = "Building upgraded";
    private final ProcessInstanceProducerProvider processInstanceProducerProvider;
    private final RuntimeService runtimeService;

    @EventListener(condition = "#event.action == T(com.buffalosoftware.api.event.BuildingAction).upgraded")
    public void onBuildingEventUpgraded(BuildingEvent event) {
        if(event.getBuilding().canProduceResources()) {
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                    .variableValueEquals(RESOURCE_PRODUCTION_NAME.name(), event.getBuilding().getProductionResource().name())
                    .list();
            processInstances.forEach(instance -> runtimeService.deleteProcessInstance(instance.getProcessInstanceId(), BUILDING_UPGRADED));
            LOGGER.info("Process instance deleted due to: " + BUILDING_UPGRADED);
            processInstanceProducerProvider.getProducer(ProcessType.production)
                    .createProcessInstance(ProcessInstanceVariablesDto.builder()
                            .variable(CITY_ID, event.getCityId())
                            .variable(RESOURCE_PRODUCTION_TIME, event.getBuilding().getResourceProductionTimeForLevel(event.getLevel()))
                            .variable(RESOURCE_PRODUCTION_NAME, event.getBuilding().getProductionResource().name())
                            .variable(RESOURCE_PRODUCTION_AMOUNT, BASE_PRODUCTION_AMOUNT)
                            .build());
            LOGGER.info("New production process instance created [cityId={}] [resource={}] [time={}] [amount={}].",
                    event.getCityId(),
                    event.getBuilding().getProductionResource(),
                    event.getBuilding().getResourceProductionTimeForLevel(event.getLevel()),
                    BASE_PRODUCTION_AMOUNT);
        }
    }
}

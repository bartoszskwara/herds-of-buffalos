package com.buffalosoftware.event;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.api.event.BuildingEvent;
import com.buffalosoftware.api.event.ConstructionEvent;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.TaskEntity;
import com.buffalosoftware.processengine.instance.ProcessInstanceProducerProvider;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static com.buffalosoftware.api.event.ConstructionEvent.ConstructionAction.created;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.BUILDING_CONSTRUCTION_TIME;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_AMOUNT;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_NAME;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_TIME;
import static com.buffalosoftware.entity.Resource.BASE_PRODUCTION_AMOUNT;

@Service
@RequiredArgsConstructor
public class ConstructionEventListener {

    private final Logger LOGGER = LoggerFactory.getLogger(ConstructionEventListener.class);
    private final ProcessInstanceProducerProvider processInstanceProducerProvider;
    private final ICityService cityService;

    @EventListener
    public void onConstructionEvent(ConstructionEvent event) {
        startNextConstructionTaskIfNotInProgress(event.getCityId());
    }

    private void startNextConstructionTaskIfNotInProgress(Long cityId) {
        var city = cityService.findCityById(cityId);
        if(isConstructionInProgressInBuilding(city)) {
            return;
        }
        city.getConstructions().stream()
                .filter(recruitment -> recruitment.getStatus().pending())
                .min(Comparator.comparing(TaskEntity::getCreationDate))
                .ifPresent(construction -> {
                    processInstanceProducerProvider.getProducer(ProcessType.construction)
                            .createProcessInstance(ProcessInstanceVariablesDto.builder()
                                    .variable(CONSTRUCTION_ID, construction.getId())
                                    .variable(BUILDING_CONSTRUCTION_TIME, construction.getBuilding().getConstructionTimeForLevel(construction.getLevel()))
                                    .variable(CITY_ID, city.getId())
                                    .build());
                    LOGGER.info("Construction [id={}] started.", construction.getId());
                });
    }

    private boolean isConstructionInProgressInBuilding(City city) {
        return city.getConstructions().stream().anyMatch(construction -> construction.getStatus().inProgress());
    }
}

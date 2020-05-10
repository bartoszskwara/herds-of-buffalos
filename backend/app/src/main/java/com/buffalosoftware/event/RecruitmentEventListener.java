package com.buffalosoftware.event;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.api.event.ConstructionEvent;
import com.buffalosoftware.api.event.RecruitmentEvent;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.TaskEntity;
import com.buffalosoftware.processengine.instance.ProcessInstanceProducerProvider;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.BUILDING_CONSTRUCTION_TIME;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_AMOUNT_LEFT;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_RECRUITMENT_TIME;

@Service
@RequiredArgsConstructor
public class RecruitmentEventListener {

    private final Logger LOGGER = LoggerFactory.getLogger(RecruitmentEventListener.class);
    private final ProcessInstanceProducerProvider processInstanceProducerProvider;
    private final ICityService cityService;

    @EventListener
    public void onRecruitmentEvent(RecruitmentEvent event) {
        startNextRecruitmentTaskIfNotInProgress(event.getCityId(), event.getCityBuildingId());
    }

    public void startNextRecruitmentTaskIfNotInProgress(Long cityId, Long cityBuildingId) {
        var city = cityService.findCityById(cityId);
        var cityBuilding = findCityBuilding(city, cityBuildingId);
        startNextRecruitmentTaskIfNotInProgress(city, cityBuilding);
    }

    private CityBuilding findCityBuilding(City city, Long cityBuildingId) {
        return city.getCityBuildings().stream()
                .filter(b -> b.getId().equals(cityBuildingId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City building not found!"));
    }

    private void startNextRecruitmentTaskIfNotInProgress(City city, CityBuilding cityBuilding) {
        if(isRecruitmentInProgressInBuilding(cityBuilding)) {
            return;
        }
        cityBuilding.getRecruitments().stream()
                .filter(recruitment -> recruitment.getStatus().pending())
                .min(Comparator.comparing(TaskEntity::getCreationDate))
                .ifPresent(recruitment -> {
                    processInstanceProducerProvider.getProducer(ProcessType.recruitment)
                            .createProcessInstance(ProcessInstanceVariablesDto.builder()
                                    .variable(RECRUITMENT_ID, recruitment.getId())
                                    .variable(UNIT_AMOUNT_LEFT, recruitment.getAmount())
                                    .variable(UNIT_RECRUITMENT_TIME, recruitment.getUnit().getRecruitmentTimeForLevel(recruitment.getLevel()))
                                    .variable(CITY_ID, city.getId())
                                    .variable(CITY_BUILDING_ID, cityBuilding.getId())
                                    .build());
                    LOGGER.info("Recruitment [id={}] started.", recruitment.getId());
                });
    }

    private boolean isRecruitmentInProgressInBuilding(CityBuilding cityBuilding) {
        return cityBuilding.getRecruitments().stream().anyMatch(recruitment -> recruitment.getStatus().inProgress());
    }

}

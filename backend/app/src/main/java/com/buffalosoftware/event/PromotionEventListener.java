package com.buffalosoftware.event;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.api.event.ConstructionEvent;
import com.buffalosoftware.api.event.PromotionEvent;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.TaskEntity;
import com.buffalosoftware.processengine.instance.ProcessInstanceProducerProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.BUILDING_CONSTRUCTION_TIME;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.PROMOTION_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_PROMOTION_TIME;

@Service
@RequiredArgsConstructor
public class PromotionEventListener {

    private final Logger LOGGER = LoggerFactory.getLogger(PromotionEventListener.class);
    private final ProcessInstanceProducerProvider processInstanceProducerProvider;
    private final ICityService cityService;

    @EventListener
    public void onConstructionEvent(PromotionEvent event) {
        startNextPromotionTaskIfNotInProgress(event.getCityId(), event.getCityBuildingId());
    }

    public void startNextPromotionTaskIfNotInProgress(Long cityId, Long cityBuildingId) {
        var city = cityService.findCityById(cityId);
        var cityBuilding = findCityBuilding(city, cityBuildingId);
        startNextPromotionTaskIfNotInProgress(city, cityBuilding);
    }

    private CityBuilding findCityBuilding(City city, Long cityBuildingId) {
        return city.getCityBuildings().stream()
                .filter(b -> b.getId().equals(cityBuildingId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City building not found!"));
    }

    private void startNextPromotionTaskIfNotInProgress(City city, CityBuilding cityBuilding) {
        if(isPromotionInProgressInBuilding(cityBuilding)) {
            return;
        }
        cityBuilding.getPromotions().stream()
                .filter(recruitment -> recruitment.getStatus().pending())
                .min(Comparator.comparing(TaskEntity::getCreationDate))
                .ifPresent(promotion -> {
                    processInstanceProducerProvider.getProducer(ProcessType.promotion)
                            .createProcessInstance(ProcessInstanceVariablesDto.builder()
                                    .variable(PROMOTION_ID, promotion.getId())
                                    .variable(UNIT_PROMOTION_TIME, promotion.getUnit().getPromotionTimeForLevel(promotion.getLevel()))
                                    .variable(CITY_ID, city.getId())
                                    .variable(CITY_BUILDING_ID, cityBuilding.getId())
                                    .build());
                    LOGGER.info("Promotion [id={}] started.", promotion.getId());
                });
    }

    private boolean isPromotionInProgressInBuilding(CityBuilding cityBuilding) {
        return cityBuilding.getPromotions().stream().anyMatch(promotion -> promotion.getStatus().inProgress());
    }

}

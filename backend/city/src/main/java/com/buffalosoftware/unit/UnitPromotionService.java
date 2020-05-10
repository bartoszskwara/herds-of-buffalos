package com.buffalosoftware.unit;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.event.IEventService;
import com.buffalosoftware.api.event.PromotionEvent;
import com.buffalosoftware.api.processengine.IProcessInstanceProducerProvider;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.api.unit.IUnitPromotionService;
import com.buffalosoftware.common.CostMapper;
import com.buffalosoftware.dto.ProgressTaskType;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.dto.unit.PromotionProgressDto;
import com.buffalosoftware.dto.unit.UnitPromotionRequestDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.CityBuildingUnitLevel;
import com.buffalosoftware.entity.Promotion;
import com.buffalosoftware.entity.TaskEntity;
import com.buffalosoftware.repository.CityBuildingRepository;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.PromotionRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.PROMOTION_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_PROMOTION_TIME;
import static com.buffalosoftware.entity.TaskStatus.Pending;

@Service
@RequiredArgsConstructor
public class UnitPromotionService implements IUnitPromotionService {

    private final ResourceService resourceService;
    private final CityRepository cityRepository;
    private final CityBuildingRepository cityBuildingRepository;
    private final PromotionRepository promotionRepository;
    private final ITimeService timeService;
    private final IEventService eventService;

    @Override
    public void promoteUnit(Long promotionId) {
        var promotionTask = promotionRepository.findById(promotionId).orElseThrow(() -> new IllegalArgumentException("Promotion not found!"));
        var cityBuilding = promotionTask.getCityBuilding();
        CityBuildingUnitLevel newUnitLevel = CityBuildingUnitLevel.builder()
                .cityBuilding(cityBuilding)
                .availableLevel(promotionTask.getLevel())
                .unit(promotionTask.getUnit())
                .build();
        cityBuilding.getUnitLevels().add(newUnitLevel);
        cityBuildingRepository.save(cityBuilding);
    }

    @Override
    public void createPromotionTaskAndStartProcess(Long userId, Long cityId, UnitPromotionRequestDto unitPromotionRequestDto) {
        City city = findCityByIdAndUserId(userId, cityId);
        Unit unit = Unit.getByKey(unitPromotionRequestDto.getUnit()).orElseThrow(() -> new IllegalArgumentException("Unit doesn't exist!"));
        CityBuilding cityBuilding = findCityBuilding(city, unit.getBuilding());

        Integer level = calculateNextLevel(cityBuilding, unit);
        ResourcesDto cost = CostMapper.mapCost(unit.getUpgradingCostForLevel(level));
        validateUnitPromotionConditions(city, cityBuilding, unit, level, cost);

        Promotion promotion = Promotion.builder()
                .unit(unit)
                .level(level)
                .cityBuilding(cityBuilding)
                .creationDate(timeService.now())
                .status(Pending)
                .build();
        cityBuilding.getPromotions().add(promotion);
        resourceService.decreaseResources(city, cost);
        cityRepository.save(city);

        eventService.sendEvent(PromotionEvent.builder().source(this)
                .cityId(cityId)
                .cityBuildingId(cityBuilding.getId())
                .build());
    }

    @Override
    public List<PromotionProgressDto> getPromotionProgressByBuilding(City city, Building building) {
        var cityBuilding = city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building does not exist in the city!"));
        return findAndMapNotCompletedPromotions(cityBuilding.getPromotions());
    }

    private List<PromotionProgressDto> findAndMapNotCompletedPromotions(Set<Promotion> promotions) {
        return promotions.stream()
                .filter(promotion -> promotion.getStatus().notCompleted())
                .map(promotion -> {
                    Long taskDuration = calculateTaskDuration(promotion);
                    Long creationDate = timeService.toMillis(promotion.getCreationDate());
                    Long startDate = timeService.toMillis(promotion.getStartDate());
                    return PromotionProgressDto.builder()
                            .id(promotion.getId())
                            .unit(promotion.getUnit())
                            .level(promotion.getLevel())
                            .label(promotion.getUnit().getName())
                            .startDate(startDate)
                            .taskDuration(taskDuration)
                            .type(ProgressTaskType.promotion)
                            .creationDate(creationDate)
                            .status(promotion.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Long calculateTaskDuration(Promotion promotion) {
        if(promotion == null) {
            return 0L;
        }
        long promotionTime = promotion.getUnit().getPromotionTimeForLevel(promotion.getLevel());
        long timeSpent = Optional.ofNullable(promotion.getStartDate())
                .map(startDate -> timeService.nowMillis() - timeService.toMillis(promotion.getStartDate()))
                .orElse(0L);
        return Math.max(promotionTime - timeSpent, 0L);
    }

    private City findCityByIdAndUserId(Long userId, Long cityId) {
        return cityRepository.findByIdAndUser_Id(cityId, userId)
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
    }

    private CityBuilding findCityBuilding(City city, Building building) {
        return city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building required for the unit doesn't exist in city!"));
    }

    private Integer calculateNextLevel(CityBuilding cityBuilding, Unit unit) {
        return cityBuilding.getUnitLevels().stream()
                .filter(unitLevel -> unit.equals(unitLevel.getUnit()))
                .max(Comparator.comparing(CityBuildingUnitLevel::getAvailableLevel))
                .map(l -> l.getAvailableLevel() + 1)
                .orElse(1);
    }

    private void validateUnitPromotionConditions(City city, CityBuilding cityBuilding, Unit unit, Integer level, ResourcesDto cost) {
        if(level > 1 && !isLevelEnabled(unit, level - 1, cityBuilding)) {
            throw new IllegalArgumentException("Previous level not enabled!");
        }
        if(level > unit.getMaxLevel()) {
            throw new IllegalArgumentException("Level not available!");
        }
        if(!resourceService.doesCityHaveEnoughResources(city.getCityResources(), cost)) {
            throw new IllegalArgumentException("Not enough resources!");
        }
    }

    private boolean isLevelEnabled(Unit unit, Integer level, CityBuilding cityBuilding) {
        return cityBuilding.getUnitLevels().stream()
                .anyMatch(u -> unit.equals(u.getUnit()) && level.equals(u.getAvailableLevel()));
    }
}

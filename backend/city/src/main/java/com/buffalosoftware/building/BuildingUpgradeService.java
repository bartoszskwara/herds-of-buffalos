package com.buffalosoftware.building;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.city.IBuildingUpgradeService;
import com.buffalosoftware.api.event.BuildingEvent;
import com.buffalosoftware.api.event.ConstructionEvent;
import com.buffalosoftware.api.event.IEventService;
import com.buffalosoftware.common.CostMapper;
import com.buffalosoftware.dto.building.BuildingUpgradeRequestDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.Construction;
import com.buffalosoftware.entity.TaskStatus;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.ConstructionRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.event.BuildingAction.upgraded;
import static com.buffalosoftware.api.event.ConstructionEvent.ConstructionAction.created;

@Service
@RequiredArgsConstructor
public class BuildingUpgradeService implements IBuildingUpgradeService {

    private final CityRepository cityRepository;
    private final ResourceService resourceService;
    private final ConstructionRepository constructionRepository;
    private final ITimeService timeService;
    private final IEventService eventService;

    @Override
    public void upgradeBuilding(Long constructionId) {
        var constructionTask = constructionRepository.findById(constructionId).orElseThrow(() -> new IllegalArgumentException("Construction not found!"));
        var city = constructionTask.getCity();
        var cityBuilding = city.getCityBuildings().stream().filter(cb -> constructionTask.getBuilding().equals(cb.getBuilding())).findFirst();

        cityBuilding.ifPresentOrElse(
                CityBuilding::increaseLevel,
                () -> {
                    var newCityBuilding = CityBuilding.builder()
                            .city(city)
                            .building(constructionTask.getBuilding())
                            .level(constructionTask.getLevel())
                            .creationDate(timeService.now())
                            .build();
                    city.getCityBuildings().add(newCityBuilding);
                });
        cityRepository.save(city);
        eventService.sendEvent(BuildingEvent.builder().source(this)
                .building(constructionTask.getBuilding())
                .cityId(city.getId())
                .level(constructionTask.getLevel())
                .action(upgraded).build());
    }

    @Override
    public void createConstructionTaskAndStartProcess(Long userId, Long cityId, BuildingUpgradeRequestDto buildingUpgradeRequestDto) {
        var city = findCityByIdAndUserId(userId, cityId);
        var building = findBuilding(buildingUpgradeRequestDto.getBuilding());
        var cityBuilding = city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElse(null);
        var cost = CostMapper.mapCost(building.getUpgradingCostForLevel(buildingUpgradeRequestDto.getLevel()));
        validateBuildingUpgradeConditions(city, cityBuilding, building, buildingUpgradeRequestDto.getLevel(), cost);
        Construction construction = Construction.builder()
                .building(building)
                .level(buildingUpgradeRequestDto.getLevel())
                .city(city)
                .status(TaskStatus.Pending)
                .creationDate(timeService.now())
                .build();
        city.getConstructions().add(construction);
        resourceService.decreaseResources(city, cost);
        cityRepository.save(city);
        eventService.sendEvent(ConstructionEvent.builder().source(this)
                .cityId(cityId)
                .action(created).build());
    }

    private City findCityByIdAndUserId(Long userId, Long cityId) {
        return cityRepository.findByIdAndUser_Id(cityId, userId)
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
    }

    private Building findBuilding(String building) {
        return Building.getByKey(building).orElseThrow(() -> new IllegalArgumentException("Building doesn't exist!"));
    }

    private void validateBuildingUpgradeConditions(City city, CityBuilding cityBuilding, Building building, Integer level, ResourcesDto cost) {
        if(level == 1 && cityBuilding != null) {
            throw new IllegalArgumentException("Invalid level!");
        }
        if(level > 1 && cityBuilding == null && !isUnderConstruction(city, building, level - 1)) {
            throw new IllegalArgumentException("Invalid level!");
        }
        if(level > 1 && cityBuilding != null && !cityBuilding.getLevel().equals(level - 1) && !isUnderConstruction(city, building, level - 1)) {
            throw new IllegalArgumentException("Previous level not enabled!");
        }
        if(level > building.getMaxLevel()) {
            throw new IllegalArgumentException("Level not available!");
        }
        if(!resourceService.doesCityHaveEnoughResources(city.getCityResources(), cost)) {
            throw new IllegalArgumentException("Not enough resources!");
        }
    }

    private boolean isUnderConstruction(City city, Building building, Integer level) {
        return city.getConstructions().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .filter(c -> c.getLevel().equals(level))
                .anyMatch(c -> c.getStatus().notCompleted());
    }
}

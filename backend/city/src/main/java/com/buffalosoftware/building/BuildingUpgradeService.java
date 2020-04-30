package com.buffalosoftware.building;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.city.IBuildingUpgradeService;
import com.buffalosoftware.api.processengine.IProcessInstanceProducerProvider;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.common.CostMapper;
import com.buffalosoftware.dto.building.BuildingUpgradeRequestDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.Construction;
import com.buffalosoftware.entity.TaskEntity;
import com.buffalosoftware.entity.TaskStatus;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.ConstructionRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.BUILDING_CONSTRUCTION_TIME;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;

@Service
@RequiredArgsConstructor
public class BuildingUpgradeService implements IBuildingUpgradeService {

    private final CityRepository cityRepository;
    private final ResourceService resourceService;
    private final ConstructionRepository constructionRepository;
    private final ITimeService timeService;
    private final IProcessInstanceProducerProvider processInstanceProducerProvider;
    private final RuntimeService runtimeService;

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
        startNextConstructionTaskIfNotInProgress(city);
    }

    @Override
    public void startNextConstructionTaskIfNotInProgress(Long cityId) {
        var city = findCityById(cityId);
        startNextConstructionTaskIfNotInProgress(city);
    }

    private void startNextConstructionTaskIfNotInProgress(City city) {
        if(isConstructionInProgressInBuilding(city)) {
            return;
        }
        city.getConstructions().stream()
                .max(Comparator.comparing(TaskEntity::getCreationDate))
                .ifPresent(construction -> processInstanceProducerProvider.getProducer(ProcessType.construction)
                        .createProcessInstance(ProcessInstanceVariablesDto.builder()
                                .variable(CONSTRUCTION_ID, construction.getId())
                                .variable(BUILDING_CONSTRUCTION_TIME, construction.getBuilding().getConstructionTimeForLevel(construction.getLevel()))
                                .variable(CITY_ID, city.getId())
                                .build()));
    }

    private boolean isConstructionInProgressInBuilding(City city) {
        return runtimeService.createExecutionQuery()
                .variableValueEquals(CITY_ID.name(), city.getId())
                .list()
                .size() > 0;
    }

    private City findCityById(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
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
        if(level > 1 && !cityBuilding.getLevel().equals(level - 1)) {
            throw new IllegalArgumentException("Previous level not enabled!");
        }
        if(level > building.getMaxLevel()) {
            throw new IllegalArgumentException("Level not available!");
        }
        if(!resourceService.doesCityHaveEnoughResources(city.getCityResources(), cost)) {
            throw new IllegalArgumentException("Not enough resources!");
        }
    }
}

package com.buffalosoftware.building;

import com.buffalosoftware.api.TimeService;
import com.buffalosoftware.api.city.IBuildingUpgradeService;
import com.buffalosoftware.common.CostMapper;
import com.buffalosoftware.dto.building.BuildingUpgradeRequestDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.Construction;
import com.buffalosoftware.entity.TaskStatus;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuildingUpgradeService implements IBuildingUpgradeService {

    private final CityRepository cityRepository;
    private final ResourceService resourceService;
    private final TimeService timeService;

    @Override
    public void upgradeBuilding(Long userId, Long cityId, BuildingUpgradeRequestDto buildingUpgradeRequestDto) {
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

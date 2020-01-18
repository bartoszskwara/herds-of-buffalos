package com.buffalosoftware.api.city;

import com.buffalosoftware.dto.building.BuildingNextLevelDto;
import com.buffalosoftware.dto.building.BuildingUpgradeRequestDto;
import com.buffalosoftware.dto.building.CityBuildingDto;

import java.util.List;

public interface IBuildingService {
    List<CityBuildingDto> getCityBuildings(Long userId, Long cityId);
    List<BuildingNextLevelDto> getUpgradePossibilities(Long userId, Long cityId);
    void upgradeBuilding(Long userId, Long cityId, BuildingUpgradeRequestDto buildingUpgradeRequestDto);
}

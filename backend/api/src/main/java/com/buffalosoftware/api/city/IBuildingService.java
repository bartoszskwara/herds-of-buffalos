package com.buffalosoftware.api.city;

import com.buffalosoftware.dto.building.BuildingNextLevelDto;
import com.buffalosoftware.dto.building.CityBuildingDto;
import com.buffalosoftware.dto.building.UnitRecruitmentDto;
import com.buffalosoftware.entity.Building;

import java.util.List;

public interface IBuildingService {
    List<CityBuildingDto> getCityBuildings(Long userId, Long cityId);
    List<BuildingNextLevelDto> getUpgradePossibilities(Long userId, Long cityId);
    List<UnitRecruitmentDto> getAvailableUnits(Long userId, Long cityId, Building building);
}

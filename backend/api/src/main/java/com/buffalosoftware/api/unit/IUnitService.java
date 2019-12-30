package com.buffalosoftware.api.unit;

import com.buffalosoftware.dto.building.CityUnitDto;
import com.buffalosoftware.dto.building.UnitUpgradeDto;
import com.buffalosoftware.dto.building.UnitWithLevelsDto;
import com.buffalosoftware.entity.Building;

import java.util.List;

public interface IUnitService {
    List<CityUnitDto> getUnitsInCity(Long userId, Long cityId);
    List<UnitWithLevelsDto> getAvailableUnits(Long userId, Long cityId, Building building);
    List<UnitUpgradeDto> getUpgradePossibilities(Long userId, Long cityId);
}

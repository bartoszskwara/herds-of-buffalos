package com.buffalosoftware.api.unit;

import com.buffalosoftware.dto.unit.UnitWithLevelsDto;
import com.buffalosoftware.entity.Building;

import java.util.List;

public interface IUnitService {
    List<UnitWithLevelsDto> getUnitsInCity(Long userId, Long cityId);
    List<UnitWithLevelsDto> getAvailableUnitsInBuilding(Long userId, Long cityId, Building building);
}

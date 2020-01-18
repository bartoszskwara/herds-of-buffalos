package com.buffalosoftware.api.unit;

import com.buffalosoftware.dto.unit.RecruitmentDto;
import com.buffalosoftware.dto.unit.UnitUpgradeDto;
import com.buffalosoftware.dto.unit.UnitWithLevelsDto;
import com.buffalosoftware.dto.unit.UnitUpgradeRequestDto;
import com.buffalosoftware.entity.Building;

import java.util.List;

public interface IUnitService {
    List<UnitWithLevelsDto> getUnitsInCity(Long userId, Long cityId);
    List<UnitWithLevelsDto> getAvailableUnits(Long userId, Long cityId, Building building);
    List<UnitUpgradeDto> getUpgradePossibilities(Long userId, Long cityId);
    void upgradeUnit(Long userId, Long cityId, UnitUpgradeRequestDto unitUpgradeRequestDto);
    void recruit(Long userId, Long cityId, RecruitmentDto recruitmentDto);
}

package com.buffalosoftware.api.city;

import com.buffalosoftware.dto.building.BuildingUpgradeRequestDto;

public interface IBuildingUpgradeService {
    void upgradeBuilding(Long userId, Long cityId, BuildingUpgradeRequestDto buildingUpgradeRequestDto);
}

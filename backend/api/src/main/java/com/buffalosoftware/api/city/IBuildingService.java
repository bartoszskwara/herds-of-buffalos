package com.buffalosoftware.api.city;

import com.buffalosoftware.dto.building.BuildingNextLevelDto;
import com.buffalosoftware.dto.building.UserBuildingDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.User;

import java.util.List;
import java.util.Optional;

public interface IBuildingService {
    List<Building> getAllAvailableBuildings();
    Optional<User> getUserWithBuildings(Long userId);
    List<UserBuildingDto> getUserBuildings(Long userId);
    List<BuildingNextLevelDto> getUpgradePossibilities(Long userId);
}

package com.buffalosoftware.city;

import com.buffalosoftware.dto.building.UserBuildingDto;
import com.buffalosoftware.entity.BuildingKey;
import com.buffalosoftware.entity.User;

import java.util.List;
import java.util.Optional;

public interface IBuildingService {
    List<BuildingKey> getAllAvailableBuildings();
    Optional<User> getUserWithBuildings(Long userId);
    List<UserBuildingDto> getUserBuildings(Long userId);
}

package com.buffalosoftware.city;

import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.User;

import java.util.List;
import java.util.Optional;

public interface IBuildingService {
    List<Building> getAllAvailableBuildings();
    Optional<User> getUserWithBuildings(Long userId);
}

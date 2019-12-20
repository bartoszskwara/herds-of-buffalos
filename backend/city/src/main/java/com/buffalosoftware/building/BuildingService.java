package com.buffalosoftware.building;

import com.buffalosoftware.city.IBuildingService;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.BuildingRepository;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingService implements IBuildingService {

    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;

    @Override
    public List<Building> getAllAvailableBuildings() {
        return buildingRepository.findAll();
    }

    @Override
    public Optional<User> getUserWithBuildings(Long userId) {
        return userRepository.findUserWithBuildingsById(userId);
    }
}

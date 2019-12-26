package com.buffalosoftware.building;

import com.buffalosoftware.api.city.IBuildingService;
import com.buffalosoftware.dto.building.BuildingDto;
import com.buffalosoftware.dto.building.BuildingNextLevelDto;
import com.buffalosoftware.dto.building.UserBuildingDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.entity.UserBuilding;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class BuildingService implements IBuildingService {

    private final UserRepository userRepository;

    @Override
    public List<Building> getAllAvailableBuildings() {
        return asList(Building.values());
    }

    @Override
    public List<UserBuildingDto> getUserBuildings(Long userId) {
        User user = userRepository.findUserWithBuildingsById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));
        Map<Building, UserBuilding> userBuildingsByKey = user.getUserBuildings().stream().collect(Collectors.toMap(UserBuilding::getBuilding, Function.identity()));

        List<UserBuildingDto> userBuildings = user.getUserBuildings().stream()
                .map(ub -> createUserBuilding(ub.getBuilding(), ub.getLevel()))
                .collect(Collectors.toList());
        List<UserBuildingDto> notBuiltBuildings = getAllAvailableBuildings().stream()
                .filter(b -> !userBuildingsByKey.keySet().contains(b))
                .map(b -> createUserBuilding(b, 0))
                .collect(Collectors.toList());
        userBuildings.addAll(notBuiltBuildings);

        return userBuildings.stream()
                .sorted(Comparator.comparing(b -> b.getBuilding().getLabel()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUserWithBuildings(Long userId) {
        return userRepository.findUserWithBuildingsById(userId);
    }

    private UserBuildingDto createUserBuilding(Building building, Integer level) {
        return UserBuildingDto.builder()
                .building(BuildingDto.builder()
                        .key(building)
                        .label(building.getName())
                        .build())
                .level(level)
                .build();
    }

    @Override
    public List<BuildingNextLevelDto> getUpgradePossibilities(Long userId) {
        User user = userRepository.findUserWithBuildingsAndResourcesById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        Map<Building, Integer> userBuildingsWithLevels = user.getUserBuildings().stream()
                .collect(Collectors.toMap(UserBuilding::getBuilding, UserBuilding::getLevel));
        return Building.list().stream()
                .map(building -> {
                    Integer nexLevel = getNextLevelOfBuilding(building, userBuildingsWithLevels);
                    ResourcesDto resources = ResourcesDto.builder()
                            .wood(building.getWoodCostForLevel(nexLevel))
                            .clay(building.getClayCostForLevel(nexLevel))
                            .iron(building.getIronCostForLevel(nexLevel))
                            .build();
                    return BuildingNextLevelDto.builder()
                            .building(BuildingDto.builder()
                                    .key(building)
                                    .label(building.getName())
                                    .maxLevel(building.getMaxLevel()).build())
                            .nextLevel(nexLevel)
                            .cost(resources)
                            .requiredBuildings(emptyList())
                            .requirementsMet(areRequirementsMetForBuilding(user, resources))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private boolean areRequirementsMetForBuilding(User user, ResourcesDto resourcesCost) {
        if(user.getUserResources().getWood() < resourcesCost.getWood()
                || user.getUserResources().getClay() < resourcesCost.getClay()
                || user.getUserResources().getIron() < resourcesCost.getIron()) {
            return false;
        }
        //TODO: add checking if required buildings are built
        return true;
    }

    private Integer getNextLevelOfBuilding(Building building, Map<Building, Integer> userBuildingsWithLevels) {
        Integer nextLevel = userBuildingsWithLevels.getOrDefault(building, 0) + 1;
        return nextLevel <= building.getMaxLevel() ? nextLevel : null;
    }


}

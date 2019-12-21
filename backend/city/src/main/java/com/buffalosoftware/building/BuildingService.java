package com.buffalosoftware.building;

import com.buffalosoftware.city.IBuildingService;
import com.buffalosoftware.dto.building.BuildingDto;
import com.buffalosoftware.dto.building.UserBuildingDto;
import com.buffalosoftware.entity.BuildingKey;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.entity.UserBuilding;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class BuildingService implements IBuildingService {

    private final UserRepository userRepository;

    @Override
    public List<BuildingKey> getAllAvailableBuildings() {
        return asList(BuildingKey.values());
    }

    @Override
    public List<UserBuildingDto> getUserBuildings(Long userId) {
        User user = userRepository.findUserWithBuildingsById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));
        Map<BuildingKey, UserBuilding> userBuildingsByKey = user.getUserBuildings().stream().collect(Collectors.toMap(UserBuilding::getBuilding, Function.identity()));

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

    private UserBuildingDto createUserBuilding(BuildingKey building, Integer level) {
        return UserBuildingDto.builder()
                .building(BuildingDto.builder()
                        .key(building)
                        .label(building.getName())
                        .build())
                .level(level)
                .build();
    }
}

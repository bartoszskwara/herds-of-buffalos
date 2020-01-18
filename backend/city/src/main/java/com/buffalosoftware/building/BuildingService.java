package com.buffalosoftware.building;

import com.buffalosoftware.Cost;
import com.buffalosoftware.api.city.IBuildingService;
import com.buffalosoftware.dto.building.BuildingDto;
import com.buffalosoftware.dto.building.BuildingNextLevelDto;
import com.buffalosoftware.dto.building.BuildingUpgradeRequestDto;
import com.buffalosoftware.dto.building.CityBuildingDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.CityBuildingRepository;
import com.buffalosoftware.repository.UserRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BuildingService implements IBuildingService {

    private final UserRepository userRepository;
    private final CityBuildingRepository cityBuildingRepository;
    private final ResourceService resourceService;

    @Override
    public List<CityBuildingDto> getCityBuildings(Long userId, Long cityId) {
        User user = userRepository.findUserWithCitiesAndBuildingsById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist"));

        Map<Building, CityBuilding> cityBuildingsByKey = city.getCityBuildings().stream()
                .collect(Collectors.toMap(CityBuilding::getBuilding, Function.identity()));
        List<CityBuildingDto> cityBuildings = city.getCityBuildings().stream()
                .map(ub -> createUserBuilding(ub.getBuilding(), ub.getLevel()))
                .collect(toList());
        List<CityBuildingDto> notBuiltBuildings = Building.list().stream()
                .filter(b -> !cityBuildingsByKey.keySet().contains(b))
                .map(b -> createUserBuilding(b, 0))
                .collect(toList());
        cityBuildings.addAll(notBuiltBuildings);

        return cityBuildings.stream()
                .sorted(Comparator.comparing(b -> b.getBuilding().getLabel()))
                .collect(toList());
    }

    private CityBuildingDto createUserBuilding(Building building, Integer level) {
        return CityBuildingDto.builder()
                .building(BuildingDto.builder()
                        .key(building)
                        .label(building.getName())
                        .build())
                .level(level)
                .build();
    }

    @Override
    public List<BuildingNextLevelDto> getUpgradePossibilities(Long userId, Long cityId) {
        User user = userRepository.findUserWithCitiesAndBuildingsAndResourcesById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist"));
        Map<Building, Integer> cityBuildingsWithLevels = city.getCityBuildings().stream()
                .collect(Collectors.toMap(CityBuilding::getBuilding, CityBuilding::getLevel));
        return Building.list().stream()
                .map(building -> createBuildingNextLevelDto(building, city, cityBuildingsWithLevels.get(building)))
                .collect(toList());
    }

    @Override
    @Transactional
    public void upgradeBuilding(Long userId, Long cityId, BuildingUpgradeRequestDto buildingUpgradeRequestDto) {
        User user = userRepository.findUserWithCitiesAndBuildingsAndResourcesById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist"));
        Building building = Building.getByKey(buildingUpgradeRequestDto.getBuilding())
                .orElseThrow(() -> new IllegalArgumentException("Building doesn't exist!"));
        CityBuilding cityBuilding = city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElse(null);

        ResourcesDto cost = mapCost(building.getUpgradingCostForLevel(buildingUpgradeRequestDto.getLevel()));
        validateBuildingUpgradeConditions(city, building, buildingUpgradeRequestDto.getLevel(), cost);
        if(cityBuilding == null) {
            if(buildingUpgradeRequestDto.getLevel() == 1) {
                CityBuilding newBuilding = new CityBuilding();
                newBuilding.setCity(city);
                newBuilding.setBuilding(building);
                newBuilding.setLevel(buildingUpgradeRequestDto.getLevel());
                cityBuildingRepository.save(newBuilding);
            } else {
                throw new IllegalArgumentException("Invalid level!");
            }
        } else {
            cityBuilding.setLevel(buildingUpgradeRequestDto.getLevel());
            cityBuildingRepository.save(cityBuilding);
        }
        resourceService.decreaseResources(city, cost);
    }

    private void validateBuildingUpgradeConditions(City city, Building building, Integer level, ResourcesDto cost) {
        if(level > 1 && !isBuildingLevelEnabled(building, level - 1, city)) {
            throw new IllegalArgumentException("Previous level not enabled!");
        }
        if(level > building.getMaxLevel()) {
            throw new IllegalArgumentException("Level not available!");
        }
        if(!resourceService.areResourceRequirementsMet(city.getCityResources(), cost)) {
            throw new IllegalArgumentException("Not enough resources!");
        }
    }

    private boolean isBuildingLevelEnabled(Building building, Integer level, City city) {
        return city.getCityBuildings().stream()
                .anyMatch(b -> building.equals(b.getBuilding()) && level.equals(b.getLevel()));
    }

    private BuildingNextLevelDto createBuildingNextLevelDto(Building building, City city, Integer currentBuildingLevel) {
        BuildingNextLevelDto.BuildingNextLevelDtoBuilder buildingNextLevelDto = BuildingNextLevelDto.builder()
                .building(BuildingDto.builder()
                        .key(building)
                        .label(building.getName())
                        .maxLevel(building.getMaxLevel()).build())
                .currentLevel(currentBuildingLevel != null ? currentBuildingLevel : 0);
        Integer nextLevel = getNextLevelOfBuilding(currentBuildingLevel, building.getMaxLevel());
        if(nextLevel != null) {
            ResourcesDto resources = ResourcesDto.builder()
                    .wood(building.getUpgradingCostForLevel(nextLevel).getWood())
                    .clay(building.getUpgradingCostForLevel(nextLevel).getClay())
                    .iron(building.getUpgradingCostForLevel(nextLevel).getIron())
                    .build();
            buildingNextLevelDto
                    .nextLevel(nextLevel)
                    .cost(resources)
                    .requiredBuildings(emptyList())
                    .requirementsMet(areRequirementsMet(city, resources));
        }
        return buildingNextLevelDto.build();
    }

    private boolean areRequirementsMet(City city, ResourcesDto resourcesCost) {
        Set<CityResources> cityResources = city.getCityResources();
        //TODO add checking buildings requirements
        return areResourceRequirementsMet(cityResources, resourcesCost);
    }

    private boolean areResourceRequirementsMet(Set<CityResources> cityResources, ResourcesDto resourcesCost) {
        return getAmountOf(wood, cityResources) >= resourcesCost.getWood()
                && getAmountOf(clay, cityResources) >= resourcesCost.getClay()
                && getAmountOf(iron, cityResources) >= resourcesCost.getIron();
    }

    private Integer getNextLevelOfBuilding(Integer currentBuildingLevel, Integer buildingMaxLevel) {
        Integer nextLevel = currentBuildingLevel != null ? currentBuildingLevel + 1 : 1;
        return nextLevel <= buildingMaxLevel ? nextLevel : null;
    }

    private Integer getAmountOf(Resource resource, Set<CityResources> resources) {
        if(isEmpty(resources) || resource == null) {
            return 0;
        }

        return resources.stream()
                .filter(r -> resource.equals(r.getResource()))
                .findFirst()
                .map(CityResources::getAmount)
                .orElse(0);
    }

    private ResourcesDto mapCost(Cost cost) {
        if(cost == null) {
            return null;
        }
        return ResourcesDto.builder()
                .wood(cost.getWood())
                .clay(cost.getClay())
                .iron(cost.getIron())
                .build();
    }

}

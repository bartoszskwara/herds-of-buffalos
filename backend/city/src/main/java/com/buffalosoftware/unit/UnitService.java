package com.buffalosoftware.unit;

import com.buffalosoftware.Cost;
import com.buffalosoftware.api.unit.IUnitService;
import com.buffalosoftware.dto.building.CityUnitDto;
import com.buffalosoftware.dto.building.UnitLevelDataDto;
import com.buffalosoftware.dto.building.UnitUpgradeDto;
import com.buffalosoftware.dto.building.UnitWithLevelsDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuildingUnitLevel;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.CityUnit;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class UnitService implements IUnitService {

    private final UserRepository userRepository;

    @Override
    public List<CityUnitDto> getUnitsInCity(Long userId, Long cityId) {
        User user = userRepository.findUserWithCitiesAndCityUnitsById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        return user.getCities().stream()
                .filter(city -> city.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"))
                .getCityUnits().stream()
                .map(units -> CityUnitDto.builder()
                        .key(units.getUnit().name())
                        .label(units.getUnit().getName())
                        .level(units.getLevel())
                        .amount(units.getAmount())
                        .building(Unit.getBuildingByUnit(units.getUnit()).map(Enum::name).orElse(null))
                        .build())
                .collect(toList());
    }

    @Override
    public List<UnitUpgradeDto> getUpgradePossibilities(Long userId, Long cityId) {
        User user = userRepository.findUserWithCitiesAndBuildingsAndUnitLevelsById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));

        Map<Unit, List<Integer>> availableUnitLevels = city.getCityBuildings().stream()
                .flatMap(cb -> cb.getUnitLevels().stream())
                .collect(groupingBy(CityBuildingUnitLevel::getUnit, mapping(CityBuildingUnitLevel::getAvailableLevel, toList())));

        return Unit.list().stream()
                .sorted(Comparator.comparing( u -> ((Unit) u).getBuilding().ordinal()).thenComparing(u -> ((Unit) u).getOrderInBuilding()))
                .map(unit -> {
                    boolean enabled = ofNullable(availableUnitLevels.get(unit)).orElse(emptyList()).contains(1);
                    UnitUpgradeDto.UnitUpgradeDtoBuilder unitUpgradeDto = UnitUpgradeDto.builder()
                            .unit(CityUnitDto.builder()
                                    .key(unit.name())
                                    .label(unit.getName())
                                    .building(Unit.getBuildingByUnit(unit).map(Building::getName).orElse(null))
                                    .build())
                            .maxLevel(unit.getMaxLevel())
                            .firstLevelSkills(unit.getSkillsForLevel(1))
                            .firstLevelRecruitmentCost(mapCost(unit.getRecruitmentCostForLevel(1)))
                            .enabled(enabled);

                    if(!enabled) {
                        ResourcesDto upgradingResources = mapCost(unit.getUpgradingCostForLevel(1));
                        unitUpgradeDto
                                .upgradingCost(upgradingResources)
                                .upgradeRequirementsMet(areRequirementsMet(city, upgradingResources));
                    }
                    return unitUpgradeDto.build();
                })
                .collect(toList());
    }

    @Override
    public List<UnitWithLevelsDto> getAvailableUnits(Long userId, Long cityId, Building building) {
        User user = userRepository.findUserWithCitiesAndBuildingsAndUnitLevelsById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
        Map<Unit, List<Integer>> unitLevelsInBuilding = city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building doesn't exist in the city!"))
                .getUnitLevels().stream()
                .collect(groupingBy(CityBuildingUnitLevel::getUnit, mapping(CityBuildingUnitLevel::getAvailableLevel, toList())));
        return Unit.getUnitsByBuilding(building).stream()
                .sorted(Comparator.comparing(Unit::getOrderInBuilding))
                .map(unit -> UnitWithLevelsDto.builder()
                        .unit(CityUnitDto.builder()
                                .key(unit.name())
                                .label(unit.getName())
                                .building(building.name())
                                .build())
                        .maxLevel(unit.getMaxLevel())
                        .levelsData(createUnitLevelsDataList(unit, unitLevelsInBuilding.get(unit), city))
                        .build())
                .collect(toList());
    }

    private List<UnitLevelDataDto> createUnitLevelsDataList(Unit unit, List<Integer> availableLevels, City city) {
        return IntStream.rangeClosed(1, unit.getMaxLevel()).boxed()
                .map(level -> {
                    Cost recruitmentCostForLevel = unit.getRecruitmentCostForLevel(level);

                    boolean levelEnabled = isNotEmpty(availableLevels) && availableLevels.contains(level);
                    boolean nextLevelEnabled = isNotEmpty(availableLevels) && availableLevels.contains(level + 1);

                    UnitLevelDataDto.UnitLevelDataDtoBuilder unitLevelDataDto = UnitLevelDataDto.builder()
                            .level(level)
                            .enabled(levelEnabled)
                            .amountInCity(city.getCityUnits().stream()
                                    .filter(u -> unit.equals(u.getUnit()) && level.equals(u.getLevel()))
                                    .findFirst()
                                    .map(CityUnit::getAmount)
                                    .orElse(0))
                            .skills(unit.getSkillsForLevel(level))
                            .recruitmentCost(ResourcesDto.builder()
                                    .wood(recruitmentCostForLevel.getWood())
                                    .clay(recruitmentCostForLevel.getClay())
                                    .iron(recruitmentCostForLevel.getIron())
                                    .build());

                    if(levelEnabled && !nextLevelEnabled && !unit.getMaxLevel().equals(level) ) {
                        Cost upgradingCostForLevel = unit.getUpgradingCostForLevel(level + 1);
                        ResourcesDto upgradingResources = ResourcesDto.builder()
                                .wood(upgradingCostForLevel.getWood())
                                .clay(upgradingCostForLevel.getClay())
                                .iron(upgradingCostForLevel.getIron())
                                .build();
                        unitLevelDataDto
                                .upgradingCost(upgradingResources)
                                .upgradeRequirementsMet(areRequirementsMet(city, upgradingResources));
                    }
                    return unitLevelDataDto.build();
                })
                .sorted(Comparator.comparing(UnitLevelDataDto::getLevel))
                .collect(toList());
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

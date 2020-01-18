package com.buffalosoftware.unit;

import com.buffalosoftware.Cost;
import com.buffalosoftware.api.unit.IUnitService;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.dto.unit.CityUnitDto;
import com.buffalosoftware.dto.unit.RecruitmentDto;
import com.buffalosoftware.dto.unit.UnitLevelDataDto;
import com.buffalosoftware.dto.unit.UnitUpgradeDto;
import com.buffalosoftware.dto.unit.UnitUpgradeRequestDto;
import com.buffalosoftware.dto.unit.UnitWithLevelsDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.CityBuildingUnitLevel;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.CityUnit;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.CityBuildingUnitLevelRepository;
import com.buffalosoftware.repository.CityUnitRepository;
import com.buffalosoftware.repository.UserRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class UnitService implements IUnitService {

    private final UserRepository userRepository;
    private final CityUnitRepository cityUnitRepository;
    private final CityBuildingUnitLevelRepository cityBuildingUnitLevelRepository;
    private final ResourceService resourceService;

    @Override
    public List<UnitWithLevelsDto> getUnitsInCity(Long userId, Long cityId) {
        User user = userRepository.findUserWithCitiesAndCityUnitsById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        Map<Unit, List<CityUnit>> unitsInCity = user.getCities().stream()
                .filter(city -> city.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"))
                .getCityUnits().stream().collect(groupingBy(CityUnit::getUnit));
        return unitsInCity.entrySet().stream()
                .map(entry -> UnitWithLevelsDto.builder()
                        .unit(CityUnitDto.builder()
                                .key(entry.getKey().name())
                                .label(entry.getKey().getName())
                                .building(Unit.getBuildingByUnit(entry.getKey()).map(Enum::name).orElse(null))
                                .build())
                        .levelsData(entry.getValue().stream()
                                .map(cityUnit -> UnitLevelDataDto.builder()
                                        .level(cityUnit.getLevel())
                                        .amountInCity(cityUnit.getAmount())
                                        .build())
                                .sorted(Comparator.comparing(UnitLevelDataDto::getLevel))
                                .collect(toList()))
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
                .map(unit -> UnitUpgradeDto.builder()
                        .unit(CityUnitDto.builder()
                                .key(unit.name())
                                .label(unit.getName())
                                .building(Unit.getBuildingByUnit(unit).map(Building::name).orElse(null))
                                .build())
                        .maxLevel(unit.getMaxLevel())
                        .levelsData(createListOfAllUnitLevels(unit, availableUnitLevels.get(unit), city))
                        .build())
                .collect(toList());
    }

    @Override
    @Transactional
    public void upgradeUnit(Long userId, Long cityId, UnitUpgradeRequestDto unitUpgradeRequestDto) {
        User user = userRepository.findUserWithCitiesById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
        Unit unit = Unit.getByKey(unitUpgradeRequestDto.getUnit()).orElseThrow(() -> new IllegalArgumentException("Unit doesn't exist!"));
        CityBuilding cityBuilding = city.getCityBuildings().stream()
                .filter(b -> unit.getBuilding().equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building required for the unit doesn't exist in city!"));

        CityBuildingUnitLevel cityBuildingUnitLevel = cityBuilding.getUnitLevels().stream()
                .filter(u -> unit.equals(u.getUnit()) && unitUpgradeRequestDto.getLevel().equals(u.getAvailableLevel()))
                .findFirst()
                .orElse(null);

        if(cityBuildingUnitLevel == null) {
            ResourcesDto cost = mapCost(unit.getUpgradingCostForLevel(unitUpgradeRequestDto.getLevel()));
            validateUnitUpgradeConditions(city, cityBuilding, unit, unitUpgradeRequestDto.getLevel(), cost);
            CityBuildingUnitLevel newUnitLevel = new CityBuildingUnitLevel();
            newUnitLevel.setCityBuilding(cityBuilding);
            newUnitLevel.setAvailableLevel(unitUpgradeRequestDto.getLevel());
            newUnitLevel.setUnit(unit);
            cityBuildingUnitLevelRepository.save(newUnitLevel);
            resourceService.decreaseResources(city, cost);
        } else {
            throw new IllegalArgumentException("Unit already enabled!");
        }
    }

    private void validateUnitUpgradeConditions(City city, CityBuilding cityBuilding, Unit unit, Integer level, ResourcesDto cost) {
        if(level > 1 && !isLevelEnabled(unit, level - 1, cityBuilding)) {
            throw new IllegalArgumentException("Previous level not enabled!");
        }
        if(level > unit.getMaxLevel()) {
            throw new IllegalArgumentException("Level not available!");
        }
        if(!resourceService.areResourceRequirementsMet(city.getCityResources(), cost)) {
            throw new IllegalArgumentException("Not enough resources!");
        }
    }

    private boolean isLevelEnabled(Unit unit, Integer level, CityBuilding cityBuilding) {
        return cityBuilding.getUnitLevels().stream()
                .anyMatch(u -> unit.equals(u.getUnit()) && level.equals(u.getAvailableLevel()));
    }

    @Override
    @Transactional
    public void recruit(Long userId, Long cityId, RecruitmentDto recruitmentDto) {
        User user = userRepository.findUserWithCitiesById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));

        Unit unit = Unit.getByKey(recruitmentDto.getUnit())
                .orElseThrow(() -> new IllegalArgumentException("Unit doesn't exist!"));
        if(recruitmentDto.getLevel() < 1 || recruitmentDto.getLevel() > unit.getMaxLevel()) {
            throw new IllegalArgumentException("Level not allowed!");
        }

        ResourcesDto cost = mapCost(unit.getRecruitmentCostForLevel(recruitmentDto.getLevel()));
        validateRecruitmentConditions(city, unit, recruitmentDto, cost);

        Optional<CityUnit> cityUnitOpt = cityUnitRepository.findByUnitAndCity_IdAndLevel(unit, city.getId(), recruitmentDto.getLevel());
        cityUnitOpt.ifPresentOrElse(
                cityUnit -> {
                    cityUnit.setAmount(cityUnit.getAmount() + recruitmentDto.getAmount());
                    cityUnitRepository.save(cityUnit);
                }, () -> {
                    CityUnit newCityUnit = new CityUnit();
                    newCityUnit.setUnit(unit);
                    newCityUnit.setLevel(recruitmentDto.getLevel());
                    newCityUnit.setAmount(recruitmentDto.getAmount());
                    newCityUnit.setCity(city);
                    cityUnitRepository.save(newCityUnit);
                });
        resourceService.decreaseResources(city, cost);
    }

    private void validateRecruitmentConditions(City city, Unit unit, RecruitmentDto recruitmentDto, ResourcesDto cost) {
        if(!isUnitEnabled(city, unit, recruitmentDto.getLevel())) {
            throw new IllegalArgumentException("Level not enabled!");
        }
        if(!resourceService.areResourceRequirementsMet(city.getCityResources(), cost)) {
            throw new IllegalArgumentException("Not enough resources!");
        }
    }

    private boolean isUnitEnabled(City city, Unit unit, Integer level) {
        return city.getCityBuildings().stream()
                .filter(b -> unit.getBuilding().equals(b.getBuilding()))
                .anyMatch(b -> b.getUnitLevels().stream()
                        .anyMatch(ul -> unit.equals(ul.getUnit()) && ul.getAvailableLevel().equals(level)));
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
                        .levelsData(createAvailableUnitLevelsList(unit, unitLevelsInBuilding.get(unit), city))
                        .build())
                .collect(toList());
    }

    private List<UnitLevelDataDto> createAvailableUnitLevelsList(Unit unit, List<Integer> availableLevels, City city) {
        if(isEmpty(availableLevels)) {
            return emptyList();
        }
        return availableLevels.stream()
                .map(level -> {
                    Cost recruitmentCostForLevel = unit.getRecruitmentCostForLevel(level);
                    ResourcesDto recruitmentCost = mapCost(recruitmentCostForLevel);

                    return UnitLevelDataDto.builder()
                            .level(level)
                            .amountInCity(city.getCityUnits().stream()
                                    .filter(u -> unit.equals(u.getUnit()) && level.equals(u.getLevel()))
                                    .findFirst()
                                    .map(CityUnit::getAmount)
                                    .orElse(0))
                            .maxToRecruit(calculateMaxNumberOfUnitsToRecruit(recruitmentCost, city.getCityResources()))
                            .skills(unit.getSkillsForLevel(level))
                            .recruitmentCost(recruitmentCost)
                            .build();
                })
                .sorted(Comparator.comparing(UnitLevelDataDto::getLevel))
                .collect(toList());
    }

    private Integer calculateMaxNumberOfUnitsToRecruit(ResourcesDto recruitmentCost, Set<CityResources> cityResources) {
        Map<Resource, Integer> amountOfResources = new HashMap<>();
        amountOfResources.put(wood, recruitmentCost.getWood());
        amountOfResources.put(clay, recruitmentCost.getClay());
        amountOfResources.put(iron, recruitmentCost.getIron());

        return Stream.of(Resource.values())
                .mapToInt(res -> {
                    Integer resInCity = resourceService.getAmountOf(res, cityResources);
                    Integer recruitmentResourceCost = amountOfResources.get(res);
                    return recruitmentResourceCost != 0 ? resInCity / recruitmentResourceCost : 0;
                })
                .min()
                .orElse(0);
    }

    private List<UnitLevelDataDto> createListOfAllUnitLevels(Unit unit, List<Integer> availableLevels, City city) {
        return IntStream.rangeClosed(1, unit.getMaxLevel()).boxed()
                .map(level -> {
                    Cost recruitmentCostForLevel = unit.getRecruitmentCostForLevel(level);
                    Cost upgradingCostForLevel = unit.getUpgradingCostForLevel(level);
                    ResourcesDto upgradingResources = ResourcesDto.builder()
                            .wood(upgradingCostForLevel.getWood())
                            .clay(upgradingCostForLevel.getClay())
                            .iron(upgradingCostForLevel.getIron())
                            .build();

                    boolean levelEnabled = isNotEmpty(availableLevels) && availableLevels.contains(level);
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
                                    .build())
                            .upgradingCost(upgradingResources)
                            .upgradeRequirementsMet(areUpgradeRequirementsMet(city, upgradingResources, level, levelEnabled, availableLevels));
                    return unitLevelDataDto.build();
                })
                .sorted(Comparator.comparing(UnitLevelDataDto::getLevel))
                .collect(toList());
    }

    private boolean areUpgradeRequirementsMet(City city, ResourcesDto resourcesCost, Integer level, boolean levelEnabled, List<Integer> availableLevels) {
        Set<CityResources> cityResources = city.getCityResources();
        boolean prevLevelEnabled = isNotEmpty(availableLevels) && availableLevels.contains(level - 1);
        //TODO add checking buildings requirements
        return resourceService.areResourceRequirementsMet(cityResources, resourcesCost) &&
                ((level.equals(1) && !levelEnabled) || (!levelEnabled && prevLevelEnabled));
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

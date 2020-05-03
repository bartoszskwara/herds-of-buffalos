package com.buffalosoftware.unit;

import com.buffalosoftware.api.unit.IUnitService;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.dto.unit.CityUnitDto;
import com.buffalosoftware.dto.unit.UnitLevelDataDto;
import com.buffalosoftware.dto.unit.UnitLevelStatus;
import com.buffalosoftware.dto.unit.UnitWithLevelsDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.CityBuildingUnitLevel;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.CityUnit;
import com.buffalosoftware.entity.Promotion;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.PromotionRepository;
import com.buffalosoftware.repository.RecruitmentRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.buffalosoftware.common.CostMapper.mapCost;
import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UnitService implements IUnitService {

    private final CityRepository cityRepository;
    private final ResourceService resourceService;

    @Override
    public List<UnitWithLevelsDto> getUnitsInCity(Long userId, Long cityId) {
        City city = findCityByIdAndUserId(userId, cityId);
        Map<Unit, List<CityUnit>> unitsInCity = city.getCityUnits().stream().collect(groupingBy(CityUnit::getUnit));
        return unitsInCity.entrySet().stream()
                .sorted(Comparator.comparing(unit -> unit.getKey().ordinal()))
                .map(entry -> UnitWithLevelsDto.builder()
                        .unit(CityUnitDto.builder()
                                .key(entry.getKey().name())
                                .label(entry.getKey().getName())
                                .building(Unit.getBuildingByUnit(entry.getKey()).map(Enum::name).orElse(null))
                                .buildingLabel(Unit.getBuildingByUnit(entry.getKey()).map(Building::getName).orElse(null))
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
    public List<UnitWithLevelsDto> getAvailableUnitsInBuilding(Long userId, Long cityId, Building building) {
        var city = findCityByIdAndUserId(userId, cityId);
        var cityBuilding = findBuildingInCity(building, city);
        return Unit.getUnitsByBuilding(building).stream()
                .map(unit -> UnitWithLevelsDto.builder()
                        .unit(mapToCityUnitDto(unit))
                        .maxLevel(unit.getMaxLevel())
                        .levelsData(getUnitLevelsInBuilding(unit, city, cityBuilding))
                        .build())
                .collect(toList());
    }

    private City findCityByIdAndUserId(Long userId, Long cityId) {
        return cityRepository.findByIdAndUser_Id(cityId, userId)
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
    }

    private CityBuilding findBuildingInCity(Building building, City city) {
        return city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building doesn't exist in city!"));
    }

    private List<UnitLevelDataDto> getUnitLevelsInBuilding(Unit unit, City city, CityBuilding cityBuilding) {
        return IntStream.rangeClosed(1, unit.getMaxLevel()).boxed()
                .map(level -> {
                    var recruitmentCost = mapCost(unit.getRecruitmentCostForLevel(level));
                    var upgradeCost = mapCost(unit.getUpgradingCostForLevel(level));
                    return UnitLevelDataDto.builder()
                            .level(level)
                            .skills(unit.getSkillsForLevel(level))
                            .status(getUnitLevelStatus(cityBuilding, unit, level))
                            .recruitmentCost(recruitmentCost)
                            .amountInCity(getAmountInCity(unit, level, city))
                            .maxToRecruit(calculateMaxToRecruit(recruitmentCost, city.getCityResources()))
                            .upgradeRequirementsMet(areUpgradeRequirementsMet(city, cityBuilding, unit, level, upgradeCost))
                            .upgradingCost(upgradeCost)
                            .build();
                })
                .collect(toList());
    }

    private UnitLevelStatus getUnitLevelStatus(CityBuilding cityBuilding, Unit unit, Integer level) {
        if(level < 1) {
            throw new IllegalArgumentException("Invalid level!");
        }
        boolean isEnabled = cityBuilding.getUnitLevels().stream()
                .anyMatch(u -> unit.equals(u.getUnit()) && level.equals(u.getAvailableLevel()));
        if(isEnabled) {
            return UnitLevelStatus.enabled;
        }

        boolean isInProgress = cityBuilding.getPromotions().stream()
                .filter(p -> p.getStatus().notCompleted())
                .anyMatch(p -> unit.equals(p.getUnit()) && level.equals(p.getLevel()));

        return isInProgress ? UnitLevelStatus.inProgress : UnitLevelStatus.disabled;
    }

    private Integer getAmountInCity(Unit unit, Integer level, City city) {
        return city.getCityUnits().stream()
                .filter(u -> unit.equals(u.getUnit()) && u.getLevel().equals(level))
                .findFirst()
                .map(CityUnit::getAmount)
                .orElse(0);
    }

    private CityUnitDto mapToCityUnitDto(Unit unit) {
       return CityUnitDto.builder()
               .key(unit.name())
               .label(unit.getName())
               .building(unit.getBuilding().name())
               .build();
    }

    private boolean areUpgradeRequirementsMet(City city, CityBuilding cityBuilding, Unit unit, Integer level, ResourcesDto upgradeCost) {
        boolean buildingsRequirementsMet = true; //TODO add checking buildings requirements
        boolean enoughResources = resourceService.doesCityHaveEnoughResources(city.getCityResources(), upgradeCost);
        if(level == 1) {
            return buildingsRequirementsMet && enoughResources;
        }
        var previousLevelStatus = getUnitLevelStatus(cityBuilding, unit, level - 1);
        return UnitLevelStatus.enabled.equals(previousLevelStatus) && buildingsRequirementsMet && enoughResources;
    }

    private Integer calculateMaxToRecruit(ResourcesDto recruitmentCost, Set<CityResources> cityResources) {
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
}

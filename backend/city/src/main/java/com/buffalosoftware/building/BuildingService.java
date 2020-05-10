package com.buffalosoftware.building;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.city.IBuildingService;
import com.buffalosoftware.api.unit.IUnitPromotionService;
import com.buffalosoftware.api.unit.IUnitRecruitmentService;
import com.buffalosoftware.dto.ProgressTaskDto;
import com.buffalosoftware.dto.ProgressTaskType;
import com.buffalosoftware.dto.building.BuildingDto;
import com.buffalosoftware.dto.building.CityBuildingDto;
import com.buffalosoftware.dto.building.CityBuildingDto.BuildingNextLevelDto;
import com.buffalosoftware.dto.building.ConstructionProgressDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.dto.unit.PromotionProgressDto;
import com.buffalosoftware.dto.unit.RecruitmentProgressDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.Construction;
import com.buffalosoftware.entity.TaskStatus;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.buffalosoftware.common.CostMapper.mapCost;
import static com.buffalosoftware.entity.Building.townHall;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class BuildingService implements IBuildingService {

    private final CityRepository cityRepository;
    private final IUnitRecruitmentService unitRecruitmentService;
    private final IUnitPromotionService unitPromotionService;
    private final ResourceService resourceService;
    private final ITimeService timeService;

    @Override
    public List<CityBuildingDto> getCityBuildings(Long userId, Long cityId) {
        var city = findCityByIdAndUserId(userId, cityId);
        Map<Building, CityBuilding> buildingsInCity = city.getCityBuildings().stream().collect(toMap(CityBuilding::getBuilding, identity()));

        return Building.list().stream()
                .map(building -> {
                    var cityBuilding = buildingsInCity.get(building);
                    return CityBuildingDto.builder()
                            .building(BuildingDto.builder()
                                    .key(building)
                                    .label(building.getName())
                                    .build())
                            .currentLevel(cityBuilding != null ? cityBuilding.getLevel() : null)
                            .maxLevel(building.getMaxLevel())
                            .nextLevel(getNextLevel(city, cityBuilding, building))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private City findCityByIdAndUserId(Long userId, Long cityId) {
        return cityRepository.findByIdAndUser_Id(cityId, userId)
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
    }

    private BuildingNextLevelDto getNextLevel(City city, CityBuilding cityBuilding, Building building) {
        var currentLevelFromCity = Optional.ofNullable(cityBuilding)
                .map(CityBuilding::getLevel)
                .orElse(0);
        var currentLevelFromConstruction = city.getConstructions().stream()
                .filter(c -> building.equals(c.getBuilding()))
                .filter(c -> c.getStatus().notCompleted())
                .max(Comparator.comparing(Construction::getLevel))
                .map(Construction::getLevel)
                .orElse(0);
        var nextLevel = Math.max(currentLevelFromCity, currentLevelFromConstruction) + 1;
        if(cityBuilding != null && cityBuilding.getBuilding().getMaxLevel().equals(cityBuilding.getLevel())) {
            return null;
        }
        var upgradeCost = mapCost(building.getUpgradingCostForLevel(nextLevel));
        var upgradeTime = building.getConstructionTimeForLevel(nextLevel);
        return BuildingNextLevelDto.builder()
                .level(nextLevel)
                .upgradeRequirementsMet(areUpgradeRequirementsMet(city, cityBuilding, building, nextLevel, upgradeCost))
                .upgradingCost(upgradeCost)
                .upgradingTime(upgradeTime)
                .build();
    }

    private boolean areUpgradeRequirementsMet(City city, CityBuilding cityBuilding, Building building, Integer level, ResourcesDto upgradeCost) {
        if(level < 1) {
            throw new IllegalArgumentException("Invalid level!");
        }
        var buildingsRequirementsMet = true; //TODO add checking buildings requirements
        var enoughResources = resourceService.doesCityHaveEnoughResources(city.getCityResources(), upgradeCost);
        if(cityBuilding == null && level == 1) {
            return buildingsRequirementsMet && enoughResources;
        }
        var isPreviousLevelEnabled = cityBuilding != null && cityBuilding.getLevel().equals(level - 1);
        var isPreviousLevelUnderConstruction = city.getConstructions().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .filter(c -> c.getLevel().equals(level - 1))
                .anyMatch(c -> c.getStatus().notCompleted());

        return (isPreviousLevelEnabled || isPreviousLevelUnderConstruction) && buildingsRequirementsMet && enoughResources;
    }

    @Override
    public List<ConstructionProgressDto> getAllConstructionsProgressInCity(Long userId, Long cityId) {
        var city = findCityByIdAndUserId(userId, cityId);

        return city.getConstructions().stream()
                .filter(construction -> construction.getStatus().notCompleted())
                .map(construction -> mapToConstructionProgressDto(city, construction))
                .sorted(Comparator
                        .comparing((ProgressTaskDto p) -> TaskStatus.getByName(p.getStatus()).getOrder())
                        .thenComparing(ProgressTaskDto::getTaskDuration))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgressTaskDto> getTasks(Long userId, Long cityId, Building building) {
        var city = findCityByIdAndUserId(userId, cityId);
        var cityBuilding = city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building does not exist in the city!"));
        List<ConstructionProgressDto> buildingProgress = findAndMapNotCompletedConstructions(city, cityBuilding);
        List<RecruitmentProgressDto> cityRecruitmentProgress = unitRecruitmentService.getRecruitmentProgressByBuilding(city, building);
        List<PromotionProgressDto> promotionProgress = unitPromotionService.getPromotionProgressByBuilding(city, building);
        List<ProgressTaskDto> tasks = new ArrayList<>();
        tasks.addAll(buildingProgress);
        tasks.addAll(cityRecruitmentProgress);
        tasks.addAll(promotionProgress);
        return tasks.stream()
                .sorted(Comparator
                        .comparing((ProgressTaskDto p) -> TaskStatus.getByName(p.getStatus()).getOrder())
                        .thenComparing(ProgressTaskDto::getCreationDate))
                .collect(toList());
    }

    private List<ConstructionProgressDto> findAndMapNotCompletedConstructions(City city, CityBuilding cityBuilding) {
        return city.getConstructions().stream()
                .filter(construction -> isTownHallOrFilterByBuilding(construction, cityBuilding))
                .filter(construction -> construction.getStatus().notCompleted())
                .map(construction -> mapToConstructionProgressDto(city, construction))
                .collect(Collectors.toList());
    }

    private boolean isTownHallOrFilterByBuilding(Construction construction, CityBuilding cityBuilding) {
        if(!townHall.equals(cityBuilding.getBuilding())) {
            return cityBuilding.getBuilding().equals(construction.getBuilding());
        } else {
            return true;
        }
    }

    private ConstructionProgressDto mapToConstructionProgressDto(City city, Construction construction) {
        Integer currentLevel = findCurrentLevelOfConstruction(city, construction);
        Long taskDuration = calculateTaskDuration(construction);
        Long creationDate = timeService.toMillis(construction.getCreationDate());
        Long startDate = timeService.toMillis(construction.getStartDate());
        return ConstructionProgressDto.builder()
                .id(construction.getId())
                .building(construction.getBuilding())
                .currentLevel(currentLevel)
                .nextLevel(construction.getLevel())
                .label(construction.getBuilding().getName())
                .taskDuration(taskDuration)
                .type(ProgressTaskType.construction)
                .creationDate(creationDate)
                .startDate(startDate)
                .endDate(creationDate + taskDuration)
                .status(construction.getStatus().name())
                .build();
    }

    private Integer findCurrentLevelOfConstruction(City city, Construction construction) {
        Optional<CityBuilding> building = city.getCityBuildings().stream()
                .filter(b -> construction.getBuilding().equals(b.getBuilding()))
                .findFirst();
        if(building.isPresent()) {
            return building.get().getLevel();
        } else {
            return construction.getLevel() - 1;
        }
    }

    private Long calculateTaskDuration(Construction construction) {
        if(construction.getBuilding() == null || construction.getLevel() == null) {
            return 0L;
        }
        long constructionTime = construction.getBuilding().getConstructionTimeForLevel(construction.getLevel());
        long timeSpent = Optional.ofNullable(construction.getStartDate())
                .map(startDate -> timeService.nowMillis() - timeService.toMillis(construction.getStartDate()))
                .orElse(0L);
        return Math.max(constructionTime - timeSpent, 0L);
    }
}

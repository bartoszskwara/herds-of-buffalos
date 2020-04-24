package com.buffalosoftware.recruitment;

import com.buffalosoftware.api.TimeService;
import com.buffalosoftware.api.unit.IUnitRecruitmentService;
import com.buffalosoftware.common.CostMapper;
import com.buffalosoftware.dto.ProgressTaskDto;
import com.buffalosoftware.dto.ProgressTaskType;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.dto.unit.RecruitmentDto;
import com.buffalosoftware.dto.unit.RecruitmentProgressDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.Recruitment;
import com.buffalosoftware.entity.TaskStatus;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.resource.ResourceService;
import com.buffalosoftware.unit.Unit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UnitRecruitmentService implements IUnitRecruitmentService {

    private final ResourceService resourceService;
    private final CityRepository cityRepository;
    private final TimeService timeService;

    @Override
    public void recruit(Long userId, Long cityId, RecruitmentDto recruitmentDto) {
        City city = findCityByIdAndUserId(userId, cityId);
        Unit unit = findUnit(recruitmentDto.getUnit());
        ResourcesDto cost = CostMapper.mapCost(unit.getRecruitmentCostForLevel(recruitmentDto.getLevel()));

        validateRecruitmentLevel(recruitmentDto.getLevel(), unit.getMaxLevel());
        validateRecruitmentConditions(city, unit, recruitmentDto, cost);

        CityBuilding cityBuilding = findCityBuilding(city, unit.getBuilding());
        Recruitment recruitment = Recruitment.builder()
                .unit(unit)
                .level(recruitmentDto.getLevel())
                .amount(recruitmentDto.getAmount())
                .amountLeft(recruitmentDto.getAmount())
                .status(TaskStatus.Pending)
                .creationDate(timeService.now())
                .cityBuilding(cityBuilding)
                .build();
        cityBuilding.getRecruitments().add(recruitment);
        resourceService.decreaseResources(city, cost, recruitmentDto.getAmount());
        cityRepository.save(city);
    }

    private City findCityByIdAndUserId(Long userId, Long cityId) {
        return cityRepository.findByIdAndUser_Id(cityId, userId)
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"));
    }

    private Unit findUnit(String unit) {
        return Unit.getByKey(unit)
                .orElseThrow(() -> new IllegalArgumentException("Unit doesn't exist!"));
    }

    private CityBuilding findCityBuilding(City city, Building building) {
        return city.getCityBuildings().stream().filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building doesn't exist!"));
    }

    private void validateRecruitmentLevel(Integer level, Integer maxLevel) {
        Optional.ofNullable(level)
                .filter(l -> l >= 1 && l <= maxLevel)
                .orElseThrow(() -> new IllegalArgumentException("Level not allowed!"));
    }

    private void validateRecruitmentConditions(City city, Unit unit, RecruitmentDto recruitmentDto, ResourcesDto cost) {
        if(!isUnitEnabled(city, unit, recruitmentDto.getLevel())) {
            throw new IllegalArgumentException("Level not enabled!");
        }
        if(!resourceService.doesCityHaveEnoughResources(city.getCityResources(), cost)) {
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
    public List<RecruitmentProgressDto> getCityRecruitmentProgress(Long userId, Long cityId) {
        var city = findCityByIdAndUserId(userId, cityId);
        return city.getCityBuildings().stream()
                .map(building -> findAndMapNotCompletedRecruitments(building.getRecruitments()))
                .flatMap(List::stream)
                .sorted(Comparator
                        .comparing((ProgressTaskDto p) -> TaskStatus.getByName(p.getStatus()).getOrder())
                        .thenComparing(ProgressTaskDto::getTaskDuration))
                .collect(toList());
    }

    @Override
    public List<RecruitmentProgressDto> getRecruitmentProgressByBuilding(City city, Building building) {
        var cityBuilding = city.getCityBuildings().stream()
                .filter(b -> building.equals(b.getBuilding()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building does not exist in the city!"));
        return findAndMapNotCompletedRecruitments(cityBuilding.getRecruitments());
    }

    private List<RecruitmentProgressDto> findAndMapNotCompletedRecruitments(Set<Recruitment> recruitments) {
        return recruitments.stream()
                .filter(recruitment -> recruitment.getStatus().notCompleted())
                .map(recruitment -> {
                    Long taskDuration = calculateTaskDuration(recruitment);
                    Long creationDate = timeService.toMillis(recruitment.getCreationDate());
                    Long startDate = timeService.toMillis(recruitment.getStartDate());
                    return RecruitmentProgressDto.builder()
                            .id(recruitment.getId())
                            .unit(recruitment.getUnit())
                            .unitLevel(recruitment.getLevel())
                            .label(recruitment.getUnit().getName())
                            .amount(recruitment.getAmountLeft())
                            .startDate(startDate)
                            .taskDuration(taskDuration)
                            .type(ProgressTaskType.recruitment)
                            .creationDate(creationDate)
                            .endDate(creationDate + taskDuration)
                            .status(recruitment.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Long calculateTaskDuration(Recruitment recruitment) {
        if(recruitment == null) {
            return 0L;
        }
        long recruitmentTime = recruitment.getUnit().getRecruitmentTimeForLevel(recruitment.getLevel()) * recruitment.getAmount();
        long timeSpent = Optional.ofNullable(recruitment.getStartDate())
                .map(startDate -> timeService.nowMillis() - timeService.toMillis(recruitment.getStartDate()))
                .orElse(0L);
        return Math.max(recruitmentTime - timeSpent, 0L);
    }

}

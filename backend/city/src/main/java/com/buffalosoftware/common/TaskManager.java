package com.buffalosoftware.common;

import com.buffalosoftware.api.TimeService;
import com.buffalosoftware.entity.*;
import com.buffalosoftware.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.buffalosoftware.entity.TaskStatus.InProgress;
import static com.buffalosoftware.entity.TaskStatus.Pending;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.math.NumberUtils.min;

@Service
@RequiredArgsConstructor
public class TaskManager {

    private final CityRepository cityRepository;
    private final TimeService timeService;

    @Scheduled(fixedDelay = 5000) //TODO: Temporary solution, to be replaced with Camunda engine in the future
    @Transactional
    public void manageRecruitments() {
        List<City> cities = cityRepository.findAll();
        cities.forEach(city -> {
            city.getCityBuildings().forEach(cityBuilding -> {
                Optional<Recruitment> recruitmentTask = cityBuilding.getRecruitments().stream()
                        .filter(r -> InProgress.equals(r.getStatus()))
                        .findFirst();

                if(recruitmentTask.isEmpty()) {
                    recruitmentTask = cityBuilding.getRecruitments().stream()
                            .filter(r -> Pending.equals(r.getStatus()))
                            .min(Comparator.comparing(Recruitment::getCreationDate));
                    recruitmentTask.ifPresent(Recruitment::start);
                }

                if(recruitmentTask.isPresent()) {
                    int maxRecruitedUnits = calculateRecruitedUnits(recruitmentTask.get()).intValue();
                    int recruitedUnits = min(recruitmentTask.get().getAmountLeft(), maxRecruitedUnits);
                    recruitmentTask.get().decreaseAmount(recruitedUnits);
                    Optional<CityUnit> cityUnitOptional = findCityUnit(city, recruitmentTask.get());
                    cityUnitOptional.ifPresent(cityUnit -> cityUnit.increaseAmount(recruitedUnits));
                    if(cityUnitOptional.isEmpty()) {
                        CityUnit newCityUnit = CityUnit.builder()
                                .unit(recruitmentTask.get().getUnit())
                                .city(city)
                                .amount(recruitedUnits)
                                .level(recruitmentTask.get().getLevel())
                                .build();
                        city.getCityUnits().add(newCityUnit);
                    }

                    if(recruitmentTask.get().getAmountLeft() == 0) {
                        recruitmentTask.get().complete();
                    }
                }
            });
        });
        cityRepository.saveAll(cities);
    }

    @Scheduled(fixedDelay = 5000) //TODO: Temporary solution, to be replaced with Camunda engine in the future
    @Transactional
    public void manageConstructions() {
        List<City> cities = cityRepository.findAll();
        cities.forEach(city -> {
            Optional<Construction> constructionTask = city.getConstructions().stream()
                    .filter(c -> InProgress.equals(c.getStatus()))
                    .findFirst();

            if(constructionTask.isEmpty()) {
                constructionTask = city.getConstructions().stream()
                        .filter(r -> Pending.equals(r.getStatus()))
                        .min(Comparator.comparing(Construction::getCreationDate));
                constructionTask.ifPresent(Construction::start);
            }

            if(constructionTask.isPresent()) {
                long taskDuration  = constructionTask.get().getBuilding().getRecruitmentTimeForLevel(constructionTask.get().getLevel());
                long timeLeft = calculateTimeLeft(constructionTask.get(), taskDuration).intValue();

                if(timeLeft <= 0) {
                    var cityBuilding = findBuildingInCity(city, constructionTask.get().getBuilding());
                    var building = constructionTask.get().getBuilding();
                    var level = constructionTask.get().getLevel();
                    cityBuilding.ifPresentOrElse(
                            CityBuilding::increaseLevel,
                            () -> {
                                var newCityBuilding = CityBuilding.builder()
                                        .city(city)
                                        .building(building)
                                        .level(level)
                                        .creationDate(timeService.now())
                                        .build();
                                city.getCityBuildings().add(newCityBuilding);
                            });
                    constructionTask.get().complete();
                }
            }
        });
        cityRepository.saveAll(cities);
    }

    @Scheduled(fixedDelay = 5000) //TODO: Temporary solution, to be replaced with Camunda engine in the future
    @Transactional
    public void managePromotions() {
        List<City> cities = cityRepository.findAll();
        cities.forEach(city -> {
            city.getCityBuildings().forEach(cityBuilding -> {
                Optional<Promotion> promotionTask = cityBuilding.getPromotions().stream()
                        .filter(r -> InProgress.equals(r.getStatus()))
                        .findFirst();

                if(promotionTask.isEmpty()) {
                    promotionTask = cityBuilding.getPromotions().stream()
                            .filter(r -> Pending.equals(r.getStatus()))
                            .min(Comparator.comparing(Promotion::getCreationDate));
                    promotionTask.ifPresent(Promotion::start);
                }

                if(promotionTask.isPresent()) {
                    long taskDuration = promotionTask.get().getUnit().getPromotionTimeForLevel(promotionTask.get().getLevel());
                    long timeLeft = calculateTimeLeft(promotionTask.get(), taskDuration);
                    if(timeLeft <= 0) {
                        CityBuildingUnitLevel newUnitLevel = CityBuildingUnitLevel.builder()
                                .cityBuilding(cityBuilding)
                                .availableLevel(promotionTask.get().getLevel())
                                .unit(promotionTask.get().getUnit())
                                .build();
                        cityBuilding.getUnitLevels().add(newUnitLevel);
                        promotionTask.get().complete();
                    }
                }
            });
        });
        cityRepository.saveAll(cities);
    }

    private Long calculateRecruitedUnits(Recruitment recruitment) {
        LocalDateTime from = ofNullable(recruitment.getUpdateDate()).orElse(recruitment.getStartDate());
        Duration duration = Duration.between(from, timeService.now());
        long singleRecruitmentTime = recruitment.getUnit().getRecruitmentTimeForLevel(recruitment.getLevel());
        return duration.toMillis() / singleRecruitmentTime;
    }

    private Optional<CityUnit> findCityUnit(City city, Recruitment recruitment) {
        return city.getCityUnits().stream()
                .filter(u -> recruitment.getUnit().equals(u.getUnit()) && recruitment.getLevel().equals(u.getLevel()))
                .findFirst();
    }

    private Long calculateTimeLeft(TaskEntity taskEntity, long taskDuration) {
        var duration = Duration.between(taskEntity.getStartDate(), timeService.now());
        return taskDuration - duration.toMillis();
    }

    private Optional<CityBuilding> findBuildingInCity(City city, Building building) {
        return city.getCityBuildings().stream()
                .filter(cb -> building.equals(cb.getBuilding()))
                .findFirst();
    }
}

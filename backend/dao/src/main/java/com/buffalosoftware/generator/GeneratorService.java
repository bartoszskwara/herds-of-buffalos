package com.buffalosoftware.generator;

import com.buffalosoftware.api.TimeService;
import com.buffalosoftware.api.generator.IGeneratorService;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.CityBuildingUnitLevel;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.CityUnit;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.CityBuildingRepository;
import com.buffalosoftware.repository.CityBuildingUnitLevelRepository;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.CityResourcesRepository;
import com.buffalosoftware.repository.CityUnitRepository;
import com.buffalosoftware.repository.UserRepository;
import com.buffalosoftware.unit.Unit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class GeneratorService implements IGeneratorService {

    private final UserRepository userRepository;
    private final CityBuildingRepository cityBuildingRepository;
    private final CityResourcesRepository cityResourcesRepository;
    private final CityRepository cityRepository;
    private final CityUnitRepository cityUnitRepository;
    private final CityBuildingUnitLevelRepository cityBuildingUnitLevelRepository;

    private final TimeService timeService;

    private static final List<String> users;
    private final static List<Building> buildingsWithoutTownHall = Building.list().stream()
            .filter(b -> !Building.townHall.equals(b)).collect(Collectors.toList());

    @Value("${spring.jpa.hibernate.ddlAuto}")
    private String databaseInitState;

    static {
        users = asList(
                "Elwin Miajyre",
                "Hagwin Umedove",
                "Hagred Petmoira",
                "Ruvaen Orimenor",
                "Cohnal Maghana",
                "Lhoris Genlana",
                "Scalanis Daren",
                "Tanyl Petzorwyn"
        );
    }

    @PostConstruct
    public void init() {
        if(StringUtils.containsIgnoreCase(databaseInitState, "create")) {
            generate();
        }
    }

    @Override
    public void generate() {
        cityBuildingUnitLevelRepository.deleteAll();
        cityBuildingRepository.deleteAll();
        cityResourcesRepository.deleteAll();
        cityUnitRepository.deleteAll();
        cityRepository.deleteAll();
        userRepository.deleteAll();

        AtomicInteger numberOfAvailableAdditionalPlaces = new AtomicInteger(13*19 - users.size());
        List<Point2D> points = new ArrayList<>();
        LongStream.rangeClosed(0, 19L).forEach(x -> {
            LongStream.rangeClosed(0, 13L).forEach(y -> {
                points.add(new Point2D.Double(x, y));
            });
        });
        users.forEach(name -> {
            User user = new User();
            user.setName(name);
            user.setEmail(name.toLowerCase().replaceAll("\\s", ".").concat("@buffalo.com"));
            User savedUser = userRepository.save(user);

            createCityWithBuildingsAndResourcesAndUnits(points, savedUser);
            int additionalCities = new Random().ints(0, 2).findFirst().orElse(1);
            numberOfAvailableAdditionalPlaces.set(numberOfAvailableAdditionalPlaces.get() - additionalCities);
            if(numberOfAvailableAdditionalPlaces.get() >= 0) {
                for(int i = 0; i < additionalCities; i++) {
                    createCityWithBuildingsAndResourcesAndUnits(points, savedUser);
                }
            }
        });

    }

    private void createCityWithBuildingsAndResourcesAndUnits(List<Point2D> points, User savedUser) {
        int i = new Random().ints(0, points.size()).findFirst().getAsInt();
        Long x = (long) points.get(i).getX();
        Long y = (long) points.get(i).getY();
        City city = City.builder()
                .user(savedUser)
                .name("City of " + savedUser.getName() + " " + RandomStringUtils.randomAlphabetic(4))
                .coordsX(x)
                .coordsY(y)
                .points(new Random().longs(100L, 10000L).findFirst().orElse(0L))
                .build();
        createBuildingsInCity(city);
        createResourcesInCity(city);
        cityRepository.save(city);
        points.remove(i);
    }

    private void createBuildingsInCity(City city) {
        //TownHall is in every city
        CityBuilding townhall = CityBuilding.builder()
                .city(city)
                .building(Building.townHall)
                .level(new Random().ints(1, Building.townHall.getMaxLevel()).findFirst().orElse(1))
                .creationDate(LocalDateTime.now())
                .build();
        city.getCityBuildings().add(townhall);
        createUnitsInCityBuilding(Building.townHall, townhall, city);

        Set<CityBuilding> set = new HashSet<>();
        List<CityBuilding> list = new ArrayList<>();
        Collections.shuffle(buildingsWithoutTownHall);
        buildingsWithoutTownHall.stream()
                .limit(new Random().ints(1, buildingsWithoutTownHall.size()).findFirst().orElse(0))
                .forEach(building -> {
                    CityBuilding cityBuilding = CityBuilding.builder()
                            .city(city)
                            .building(building)
                            .level(new Random().ints(1, building.getMaxLevel() + 1).findFirst().orElse(1))
                            .creationDate(LocalDateTime.now())
                            .build();
                    city.getCityBuildings().add(cityBuilding);
                    set.add(cityBuilding);
                    list.add(cityBuilding);
                    createUnitsInCityBuilding(building, cityBuilding, city);
                });
    }

    private void createResourcesInCity(City city) {
        Stream.of(wood, clay, iron).forEach(resource -> {
            CityResources resources = CityResources.builder()
                    .city(city)
                    .resource(resource)
                    .amount(new Random().ints(100, 50000).findFirst().orElse(0))
                    .updateDate(timeService.now())
                    .build();
            city.getCityResources().add(resources);
        });
    }

    private void createUnitsInCityBuilding(Building building, CityBuilding cityBuilding, City city) {
        List<Unit> units = Unit.getUnitsByBuilding(building);
        Collections.shuffle(units);
        units.stream()
                .limit(new Random().ints(0, units.size() + 1).findFirst().orElse(units.size()))
                .forEach(unit -> {
                    Integer level = new Random().ints(1, unit.getMaxLevel() + 1).findFirst().orElse(1);

                    IntStream.rangeClosed(1, level).boxed().forEach(l -> {
                        CityBuildingUnitLevel cityBuildingUnitLevel = CityBuildingUnitLevel.builder()
                                .unit(unit)
                                .availableLevel(l)
                                .cityBuilding(cityBuilding)
                                .build();
                        cityBuilding.getUnitLevels().add(cityBuildingUnitLevel);

                        CityUnit cityUnit = CityUnit.builder()
                                .unit(unit)
                                .level(l)
                                .amount(new Random().ints(1, 1001).findFirst().orElse(1))
                                .city(cityBuilding.getCity())
                                .build();
                        city.getCityUnits().add(cityUnit);
                    });
                });
    }
}

package com.buffalosoftware.generate;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
public class GeneratorService {

    private final UserRepository userRepository;
    private final CityBuildingRepository cityBuildingRepository;
    private final CityResourcesRepository cityResourcesRepository;
    private final CityRepository cityRepository;
    private final CityUnitRepository cityUnitRepository;
    private final CityBuildingUnitLevelRepository cityBuildingUnitLevelRepository;

    private static final List<String> users;
    private final static List<Building> buildingsWithoutTownHall = Building.list().stream()
            .filter(b -> !Building.townhall.equals(b)).collect(Collectors.toList());

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

    void generate() {
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
        City city = new City();
        city.setUser(savedUser);
        city.setName("City of " + savedUser.getName() + " " + RandomStringUtils.randomAlphabetic(4));
        city.setCoordsX(x);
        city.setCoordsY(y);
        city.setPoints(new Random().longs(100L, 10000L).findFirst().orElse(0L));
        City savedCity = cityRepository.save(city);
        createBuildingsInCity(savedCity);
        createResourcesInCity(savedCity);
        points.remove(i);
    }

    private void createBuildingsInCity(City city) {
        //TownHall is in every city
        CityBuilding townhall = new CityBuilding();
        townhall.setCity(city);
        townhall.setBuilding(Building.townhall);
        townhall.setLevel(new Random().ints(1, Building.townhall.getMaxLevel()).findFirst().orElse(1));
        townhall = cityBuildingRepository.save(townhall);
        createUnitsInCityBuilding(Building.townhall, townhall);

        Collections.shuffle(buildingsWithoutTownHall);
        buildingsWithoutTownHall.stream()
                .limit(new Random().ints(1, buildingsWithoutTownHall.size()).findFirst().orElse(0))
                .forEach(building -> {
                    CityBuilding cityBuilding = new CityBuilding();
                    cityBuilding.setCity(city);
                    cityBuilding.setBuilding(building);
                    cityBuilding.setLevel(new Random().ints(1, building.getMaxLevel() + 1).findFirst().orElse(1));
                    cityBuilding = cityBuildingRepository.save(cityBuilding);
                    createUnitsInCityBuilding(building, cityBuilding);
                });
    }

    private void createResourcesInCity(City city) {
        Stream.of(wood, clay, iron).forEach(resource -> {
            CityResources resources = new CityResources();
            resources.setCity(city);
            resources.setResource(resource);
            resources.setAmount(new Random().ints(100, 50000).findFirst().orElse(0));
            resources.setUpdateDate(new Date());
            cityResourcesRepository.save(resources);
        });
    }

    private void createUnitsInCityBuilding(Building building, CityBuilding cityBuilding) {
        List<Unit> units = Unit.getUnitsByBuilding(building);
        Collections.shuffle(units);
        units.stream()
                .limit(new Random().ints(0, units.size() + 1).findFirst().orElse(units.size()))
                .forEach(unit -> {
                    Integer level = new Random().ints(1, unit.getMaxLevel() + 1).findFirst().orElse(1);

                    IntStream.rangeClosed(1, level).boxed().forEach(l -> {
                        CityBuildingUnitLevel cityBuildingUnitLevel = new CityBuildingUnitLevel();
                        cityBuildingUnitLevel.setUnit(unit);
                        cityBuildingUnitLevel.setAvailableLevel(l);
                        cityBuildingUnitLevel.setCityBuilding(cityBuilding);
                        cityBuildingUnitLevelRepository.save(cityBuildingUnitLevel);

                        CityUnit cityUnit = new CityUnit();
                        cityUnit.setAmount(new Random().ints(1, 1001).findFirst().orElse(1));
                        cityUnit.setCity(cityBuilding.getCity());
                        cityUnit.setLevel(l);
                        cityUnit.setUnit(unit);
                        cityUnitRepository.save(cityUnit);
                    });
                });
    }
}

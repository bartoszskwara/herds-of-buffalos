package com.buffalosoftware.generate;

import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.CityBuildingRepository;
import com.buffalosoftware.repository.UserRepository;
import com.buffalosoftware.repository.CityResourcesRepository;
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
        cityBuildingRepository.deleteAll();
        cityResourcesRepository.deleteAll();
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

            createCityWithBuildingsAndResources(points, savedUser);
            int additionalCities = new Random().ints(0, 2).findFirst().orElse(1);
            numberOfAvailableAdditionalPlaces.set(numberOfAvailableAdditionalPlaces.get() - additionalCities);
            if(numberOfAvailableAdditionalPlaces.get() >= 0) {
                for(int i = 0; i < additionalCities; i++) {
                    createCityWithBuildingsAndResources(points, savedUser);
                }
            }
        });

    }

    private void createBuildingsInCity(City city) {
        //TownHall is in every city
        CityBuilding townhall = new CityBuilding();
        townhall.setCity(city);
        townhall.setBuilding(Building.townhall);
        townhall.setLevel(new Random().ints(1, Building.townhall.getMaxLevel()).findFirst().orElse(1));
        cityBuildingRepository.save(townhall);

        Collections.shuffle(buildingsWithoutTownHall);
        buildingsWithoutTownHall.stream()
                .limit(new Random().ints(1, buildingsWithoutTownHall.size()).findFirst().orElse(0))
                .forEach(building -> {
                    CityBuilding cityBuilding = new CityBuilding();
                    cityBuilding.setCity(city);
                    cityBuilding.setBuilding(building);
                    cityBuilding.setLevel(new Random().ints(1, building.getMaxLevel() + 1).findFirst().orElse(1));
                    cityBuildingRepository.save(cityBuilding);
                });
    }

    private void createCityWithBuildingsAndResources(List<Point2D> points, User savedUser) {
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

    private void createResourcesInCity(City city) {
        Stream.of(wood, clay, iron).forEach(resource -> {
            CityResources resources = new CityResources();
            resources.setCity(city);
            resources.setResource(resource);
            resources.setAmount(new Random().longs(100L, 50000L).findFirst().orElse(0L));
            resources.setUpdateDate(new Date());
            cityResourcesRepository.save(resources);
        });
    }
}

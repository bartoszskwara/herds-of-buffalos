package com.buffalosoftware.generate;

import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.entity.UserBuilding;
import com.buffalosoftware.entity.UserResources;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.UserBuildingRepository;
import com.buffalosoftware.repository.UserRepository;
import com.buffalosoftware.repository.UserResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GeneratorService {

    private final UserRepository userRepository;
    private final UserBuildingRepository userBuildingRepository;
    private final UserResourcesRepository userResourcesRepository;
    private final CityRepository cityRepository;
    private static final List<String> users;



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

    void generate() {
        userBuildingRepository.deleteAll();
        userResourcesRepository.deleteAll();
        cityRepository.deleteAll();
        userRepository.deleteAll();

        List<Building> buildings = Building.list();

        AtomicInteger numberOfAvailableAdditionalPlaces = new AtomicInteger(13*19 - users.size());
        List<Point2D> points = new ArrayList<>();
        LongStream.range(0, 20L).forEach(x -> {
            LongStream.range(0, 14L).forEach(y -> {
                points.add(new Point2D.Double(x, y));
            });
        });
        users.forEach(name -> {
            User user = new User();
            user.setName(name);
            user.setEmail(name.toLowerCase().replaceAll("\\s", ".").concat("@buffalo.com"));
            User savedUser = userRepository.save(user);

            Collections.shuffle(buildings);
            buildings.stream()
                    .limit(new Random().ints(1, buildings.size()).findFirst().orElse(0))
                    .forEach(building -> {
                        UserBuilding userBuilding = new UserBuilding();
                        userBuilding.setUser(savedUser);
                        userBuilding.setBuilding(building);
                        userBuilding.setLevel(new Random().ints(0, building.getMaxLevel()).findFirst().orElse(1));
                        userBuildingRepository.save(userBuilding);
                    });

            createResources(savedUser);

            createCity(points, savedUser);
            int additionalCities = new Random().ints(0, 2).findFirst().orElse(1);
            numberOfAvailableAdditionalPlaces.set(numberOfAvailableAdditionalPlaces.get() - additionalCities);
            if(numberOfAvailableAdditionalPlaces.get() >= 0) {
                for(int i = 0; i < additionalCities; i++) {
                    createCity(points, savedUser);
                }
            }
        });

    }

    private void createCity(List<Point2D> points, User savedUser) {
        int i = new Random().ints(0, points.size()).findFirst().getAsInt();
        Long x = (long) points.get(i).getX();
        Long y = (long) points.get(i).getY();
        City city = new City();
        city.setUser(savedUser);
        city.setName("City of " + savedUser.getName() + " " + RandomStringUtils.randomAlphabetic(4));
        city.setCoordsX(x);
        city.setCoordsY(y);
        city.setPoints(new Random().longs(100L, 10000L).findFirst().orElse(0L));
        cityRepository.save(city);
        points.remove(i);
    }

    private void createResources(User savedUser) {
        UserResources resources = new UserResources();
        resources.setUser(savedUser);
        resources.setUpdateDate(new Date());
        resources.setWood(new Random().longs(100L, 50000L).findFirst().orElse(0L));
        resources.setClay(new Random().longs(100L, 50000L).findFirst().orElse(0L));
        resources.setIron(new Random().longs(100L, 50000L).findFirst().orElse(0L));
        userResourcesRepository.save(resources);
    }
}

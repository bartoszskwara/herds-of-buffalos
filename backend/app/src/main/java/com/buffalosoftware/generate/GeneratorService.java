package com.buffalosoftware.generate;

import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.entity.UserBuilding;
import com.buffalosoftware.repository.BuildingRepository;
import com.buffalosoftware.repository.UserBuildingRepository;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class GeneratorService {

    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;
    private final UserBuildingRepository userBuildingRepository;
    private static final Map<String, Integer> buildingsWithLevels;
    private static final List<String> users;



    static {
        buildingsWithLevels = new HashMap<>();
        buildingsWithLevels.put("Town Hall", 30);
        buildingsWithLevels.put("Barracks", 20);
        buildingsWithLevels.put("Armory", 10);
        buildingsWithLevels.put("Granary", 30);
        buildingsWithLevels.put("Brickyard", 25);
        buildingsWithLevels.put("Sawmill", 25);
        buildingsWithLevels.put("Ironworks", 25);
        buildingsWithLevels.put("Pasture", 10);
        buildingsWithLevels.put("Machine Factory", 5);
        buildingsWithLevels.put("Palace", 3);
        buildingsWithLevels.put("Wall", 15);
        buildingsWithLevels.put("Market", 10);
        buildingsWithLevels.put("Well", null); //studnia
        buildingsWithLevels.put("Fountain", null); //fontanna
        buildingsWithLevels.put("Statue", null); //posąg

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
        buildingRepository.deleteAll();
        userBuildingRepository.deleteAll();
        userRepository.deleteAll();

        buildingsWithLevels.forEach((name, maxLevel)-> {
            Building building = new Building();
            building.setName(name);
            building.setMaxLevel(maxLevel);
            buildingRepository.save(building);
        });

        List<Building> buildings = buildingRepository.findAll();

        users.forEach(name -> {
            User user = new User();
            user.setName(name);
            user.setEmail(name.toLowerCase().replaceAll("\\s", ".").concat("@buffalo.com"));
            User savedUser = userRepository.save(user);

            Collections.shuffle(buildings);
            buildings.stream()
                    .limit(new Random().ints(1, buildingsWithLevels.size()).findFirst().orElse(0))
                    .forEach(building -> {
                        Integer level = building.getMaxLevel() != null ?
                                new Random().ints(1, building.getMaxLevel()).findFirst().orElse(1) :
                                null;
                        UserBuilding userBuilding = new UserBuilding();
                        userBuilding.setUser(savedUser);
                        userBuilding.setBuilding(building);
                        userBuilding.setLevel(level);
                        userBuildingRepository.save(userBuilding);
                    });


        });


    }
}

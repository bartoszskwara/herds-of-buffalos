package com.buffalosoftware.generate;

import com.buffalosoftware.entity.BuildingKey;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.entity.UserBuilding;
import com.buffalosoftware.repository.UserBuildingRepository;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class GeneratorService {

    private final UserRepository userRepository;
    private final UserBuildingRepository userBuildingRepository;
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
        userRepository.deleteAll();

        List<BuildingKey> buildings = asList(BuildingKey.values());

        users.forEach(name -> {
            User user = new User();
            user.setName(name);
            user.setEmail(name.toLowerCase().replaceAll("\\s", ".").concat("@buffalo.com"));
            user.setPoints(new Random().longs(100L, 50000L).findFirst().orElse(0L));
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


        });


    }
}

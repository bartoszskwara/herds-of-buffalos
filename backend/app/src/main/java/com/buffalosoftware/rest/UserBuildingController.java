package com.buffalosoftware.rest;

import com.buffalosoftware.city.IBuildingService;
import com.buffalosoftware.dto.building.BuildingDto;
import com.buffalosoftware.dto.building.UserBuildingDto;
import com.buffalosoftware.dto.building.UserDto;
import com.buffalosoftware.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/user/{userId}/building")
@RequiredArgsConstructor
public class UserBuildingController {

    private final IBuildingService buildingService;

    @GetMapping("")
    public ResponseEntity getAllUserBuildings(@NotNull @PathVariable("userId") Long userId) {
        User user = buildingService.getUserWithBuildings(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exists!"));
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .buildings(user.getUserBuildings().stream()
                        .map(ub -> UserBuildingDto.builder()
                                .building(BuildingDto.builder()
                                        .id(ub.getBuilding().getId())
                                        .name(ub.getBuilding().getName())
                                        .build())
                                .level(ub.getLevel()).build())
                        .collect(toList()))
                .build();

        return ResponseEntity.ok(userDto);
    }
}
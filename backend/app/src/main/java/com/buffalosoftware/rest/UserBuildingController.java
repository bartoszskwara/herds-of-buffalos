package com.buffalosoftware.rest;

import com.buffalosoftware.city.IBuildingService;
import com.buffalosoftware.dto.building.BaseDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/{userId}/building")
@RequiredArgsConstructor
public class UserBuildingController {

    private final IBuildingService buildingService;

    @GetMapping("")
    public ResponseEntity getAllUserBuildings(@NotNull @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(new BaseDtoList<>(buildingService.getUserBuildings(userId)));
    }
}
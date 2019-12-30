package com.buffalosoftware.rest;

import com.buffalosoftware.api.city.IBuildingService;
import com.buffalosoftware.dto.building.BaseDtoList;
import com.buffalosoftware.entity.Building;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/{userId}/city/{cityId}/building")
@RequiredArgsConstructor
public class CityBuildingController {

    private final IBuildingService buildingService;

    @GetMapping("")
    public ResponseEntity getAllCityBuildings(@NotNull @PathVariable("userId") Long userId,
                                              @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(new BaseDtoList<>(buildingService.getCityBuildings(userId, cityId)));
    }

    @GetMapping("/upgrade")
    public ResponseEntity getUpgradePossibilities(@NotNull @PathVariable("userId") Long userId,
                                                  @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(new BaseDtoList<>(buildingService.getUpgradePossibilities(userId, cityId)));
    }

    @GetMapping("/{buildingKey}/unit")
    public ResponseEntity getAvailableUnits(@NotNull @PathVariable("userId") Long userId,
                                            @NotNull @PathVariable("cityId") Long cityId,
                                            @NotNull @PathVariable("buildingKey") String buildingKey) {
        Building building = Building.getByKey(buildingKey).orElseThrow(() -> new IllegalArgumentException("Building doesn't exist!"));
        return ResponseEntity.ok(new BaseDtoList<>(buildingService.getAvailableUnits(userId, cityId, building)));
    }
}
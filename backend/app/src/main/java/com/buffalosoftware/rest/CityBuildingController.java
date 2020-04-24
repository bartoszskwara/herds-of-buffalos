package com.buffalosoftware.rest;

import com.buffalosoftware.api.city.IBuildingService;
import com.buffalosoftware.api.city.IBuildingUpgradeService;
import com.buffalosoftware.api.unit.IUnitService;
import com.buffalosoftware.dto.building.BaseDtoList;
import com.buffalosoftware.dto.building.BuildingUpgradeRequestDto;
import com.buffalosoftware.entity.Building;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/{userId}/city/{cityId}/building")
@RequiredArgsConstructor
public class CityBuildingController {

    private final IBuildingService buildingService;
    private final IUnitService unitService;
    private final IBuildingUpgradeService buildingUpgradeService;

    @GetMapping("")
    public ResponseEntity getAllCityBuildings(@NotNull @PathVariable("userId") Long userId,
                                              @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(new BaseDtoList<>(buildingService.getCityBuildings(userId, cityId)));
    }

    @PutMapping("")
    public ResponseEntity upgradeBuilding(@NotNull @PathVariable("userId") Long userId,
                                          @NotNull @PathVariable("cityId") Long cityId,
                                          @NotNull @RequestBody BuildingUpgradeRequestDto buildingUpgradeRequestDto) {
        buildingUpgradeService.upgradeBuilding(userId, cityId, buildingUpgradeRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/progress")
    public ResponseEntity getBuildingProgress(@NotNull @PathVariable("userId") Long userId,
                                              @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(new BaseDtoList<>(buildingService.getAllConstructionsProgressInCity(userId, cityId)));
    }

    @GetMapping("/{buildingKey}/unit")
    public ResponseEntity getUnitsInBuilding(@NotNull @PathVariable("userId") Long userId,
                                             @NotNull @PathVariable("cityId") Long cityId,
                                             @NotNull @PathVariable("buildingKey") String buildingKey) {
        Building building = Building.getByKey(buildingKey).orElseThrow(() -> new IllegalArgumentException("Building doesn't exist!"));
        return ResponseEntity.ok(new BaseDtoList<>(unitService.getAvailableUnitsInBuilding(userId, cityId, building)));
    }

    @GetMapping("/{buildingKey}/tasks")
    public ResponseEntity getTasksInBuilding(@NotNull @PathVariable("userId") Long userId,
                                             @NotNull @PathVariable("cityId") Long cityId,
                                             @NotNull @PathVariable("buildingKey") String buildingKey) {
        Building building = Building.getByKey(buildingKey).orElseThrow(() -> new IllegalArgumentException("Building doesn't exist!"));
        return ResponseEntity.ok(new BaseDtoList<>(buildingService.getTasks(userId, cityId, building)));
    }

}
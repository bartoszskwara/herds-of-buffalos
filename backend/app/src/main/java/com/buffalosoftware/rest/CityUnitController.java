package com.buffalosoftware.rest;

import com.buffalosoftware.api.unit.IUnitService;
import com.buffalosoftware.dto.building.BaseDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/{userId}/city/{cityId}/unit")
@RequiredArgsConstructor
public class CityUnitController {

    private final IUnitService unitService;

    @GetMapping("")
    public ResponseEntity getAllCityUnits(@NotNull @PathVariable("userId") Long userId,
                                          @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(new BaseDtoList<>(unitService.getUnitsInCity(userId, cityId)));
    }
}
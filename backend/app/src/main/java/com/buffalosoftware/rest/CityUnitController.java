package com.buffalosoftware.rest;

import com.buffalosoftware.api.unit.IUnitService;
import com.buffalosoftware.dto.building.BaseDtoList;
import com.buffalosoftware.dto.unit.RecruitmentDto;
import com.buffalosoftware.dto.unit.UnitUpgradeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @GetMapping("/upgrade")
    public ResponseEntity getUnitUpgradePossibilities(@NotNull @PathVariable("userId") Long userId,
                                                      @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(new BaseDtoList<>(unitService.getUpgradePossibilities(userId, cityId)));
    }

    @PostMapping("/upgrade")
    public ResponseEntity upgradeUnit(@NotNull @PathVariable("userId") Long userId,
                                      @NotNull @PathVariable("cityId") Long cityId,
                                      @Valid @RequestBody UnitUpgradeRequestDto upgradeUnitDto) {
        unitService.upgradeUnit(userId, cityId, upgradeUnitDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/")
    public ResponseEntity recruit(@NotNull @PathVariable("userId") Long userId,
                                  @NotNull @PathVariable("cityId") Long cityId,
                                  @Valid @RequestBody RecruitmentDto recruitmentDto) {
        unitService.recruit(userId, cityId, recruitmentDto);
        return ResponseEntity.ok().build();
    }
}
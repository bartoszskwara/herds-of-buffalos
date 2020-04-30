package com.buffalosoftware.rest;

import com.buffalosoftware.api.unit.IUnitPromotionService;
import com.buffalosoftware.dto.unit.UnitPromotionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/{userId}/city/{cityId}/unit/promote")
@RequiredArgsConstructor
public class UnitPromotionController {

    private final IUnitPromotionService unitPromotionService;

    @PostMapping("")
    public ResponseEntity<Void> promoteUnit(@NotNull @PathVariable("userId") Long userId,
                                      @NotNull @PathVariable("cityId") Long cityId,
                                      @Valid @RequestBody UnitPromotionRequestDto promoteUnitDto) {
        unitPromotionService.createPromotionTaskAndStartProcess(userId, cityId, promoteUnitDto);
        return ResponseEntity.ok().build();
    }
}
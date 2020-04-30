package com.buffalosoftware.rest;

import com.buffalosoftware.api.unit.IUnitRecruitmentService;
import com.buffalosoftware.dto.building.BaseDtoList;
import com.buffalosoftware.dto.unit.RecruitmentDto;
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
public class UnitRecruitmentController {

    private final IUnitRecruitmentService unitRecruitmentService;

    @PostMapping("")
    public ResponseEntity recruit(@NotNull @PathVariable("userId") Long userId,
                                  @NotNull @PathVariable("cityId") Long cityId,
                                  @Valid @RequestBody RecruitmentDto recruitmentDto) {
        unitRecruitmentService.createRecruitmentTaskAndStartProcess(userId, cityId, recruitmentDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/progress")
    public ResponseEntity getRecruitmentProgress(@NotNull @PathVariable("userId") Long userId,
                                                 @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(new BaseDtoList<>(unitRecruitmentService.getCityRecruitmentProgress(userId, cityId)));
    }
}
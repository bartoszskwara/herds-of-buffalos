package com.buffalosoftware.rest;

import com.buffalosoftware.api.city.IBuildingService;
import com.buffalosoftware.dto.building.BaseDtoList;
import com.buffalosoftware.dto.building.BuildingDto;
import com.buffalosoftware.entity.Building;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/building")
@RequiredArgsConstructor
public class BuildingController {

    private final IBuildingService buildingService;

    @GetMapping("")
    public ResponseEntity getAllAvailableBuildings() {
        List<Building> buildings = buildingService.getAllAvailableBuildings();
        List<BuildingDto> buildingDtos = buildings.stream()
                .map(b -> BuildingDto.builder()
                        .key(b)
                        .label(b.getName())
                        .maxLevel(b.getMaxLevel())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new BaseDtoList<>(buildingDtos));
    }

}
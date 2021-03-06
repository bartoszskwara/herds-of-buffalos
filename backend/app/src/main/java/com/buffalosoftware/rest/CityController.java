package com.buffalosoftware.rest;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.dto.building.BaseDtoList;
import com.buffalosoftware.dto.city.CityDto;
import com.buffalosoftware.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/{userId}/city")
@RequiredArgsConstructor
public class CityController {

    private final ICityService cityService;

    @GetMapping("")
    public ResponseEntity getAllUserCities(@NotNull @PathVariable("userId") Long userId) {
        List<CityDto> cities = cityService.getAllUserCities(userId).stream()
                .map(city -> CityDto.builder()
                        .user(UserDto.builder()
                                .id(city.getUser().getId())
                                .name(city.getUser().getName()).build())
                        .id(city.getId())
                        .name(city.getName())
                        .coordX(city.getCoordsX())
                        .coordY(city.getCoordsY())
                        .points(city.getPoints())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new BaseDtoList<>(cities));
    }

    @GetMapping("/{cityId}")
    public ResponseEntity getCityData(@NotNull @PathVariable("userId") Long userId,
                                           @NotNull @PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(cityService.getCityData(userId, cityId));
    }


}
package com.buffalosoftware.dto.city;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.building.UserDto;
import com.buffalosoftware.entity.Building;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CityDto extends BaseDto {
    private UserDto user;
    private String name;
    private Long coordX;
    private Long coordY;
}

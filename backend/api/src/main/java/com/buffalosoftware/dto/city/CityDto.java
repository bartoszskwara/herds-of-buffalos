package com.buffalosoftware.dto.city;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.building.UserDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CityDto extends BaseDto {
    private UserDto user;
    private Long id;
    private String name;
    private Long coordX;
    private Long coordY;
    private Long points;
    private ResourcesDto resources;
}

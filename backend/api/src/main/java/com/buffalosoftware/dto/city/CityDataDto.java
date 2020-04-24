package com.buffalosoftware.dto.city;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.building.CityBuildingDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CityDataDto extends BaseDto {
    private Long id;
    private String name;
    private Long points;
    private ResourcesDto resources;
    private List<CityBuildingDto> buildings;
}

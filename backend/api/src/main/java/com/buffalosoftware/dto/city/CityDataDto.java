package com.buffalosoftware.dto.city;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CityDataDto extends BaseDto {
    private Long id;
    private String name;
    private Long points;
    private ResourcesDto resources;
}

package com.buffalosoftware.dto.user;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDataDto extends BaseDto {
    private Long id;
    private String name;
    private Long points;
    private Integer numberOfCities;
    private Long ranking;
    private ResourcesDto resources;
}

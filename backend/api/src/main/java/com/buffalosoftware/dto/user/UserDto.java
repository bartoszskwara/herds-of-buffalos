package com.buffalosoftware.dto.user;

import com.buffalosoftware.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto extends BaseDto {
    private Long id;
    private String name;
    private Long points;
    private Long ranking;
    private Integer numberOfCities;
    private Long currentCityId;
}

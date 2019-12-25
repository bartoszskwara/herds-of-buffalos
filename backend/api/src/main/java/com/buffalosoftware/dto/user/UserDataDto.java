package com.buffalosoftware.dto.user;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.building.UserBuildingDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserDataDto extends BaseDto {
    private Long id;
    private String name;
    private Long points;
    private Integer numberOfCities;
    private Long ranking;
}

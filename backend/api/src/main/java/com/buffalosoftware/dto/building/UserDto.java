package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserDto extends BaseDto {
    private Long id;
    private String name;
    private List<UserBuildingDto> buildings;
}

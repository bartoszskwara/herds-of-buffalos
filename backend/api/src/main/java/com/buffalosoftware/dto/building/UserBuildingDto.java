package com.buffalosoftware.dto.building;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBuildingDto extends BaseDto {
    private BuildingDto building;
    private Integer level;
}

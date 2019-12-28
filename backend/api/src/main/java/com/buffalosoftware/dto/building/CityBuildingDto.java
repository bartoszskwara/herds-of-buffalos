package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CityBuildingDto extends BaseDto {
    private BuildingDto building;
    private Integer level;
    private boolean isUnderConstruction;
}

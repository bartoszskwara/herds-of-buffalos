package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.entity.Building;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuildingDto extends BaseDto {
    private Building key;
    private String label;
    private Integer maxLevel;
}

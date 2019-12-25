package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.entity.BuildingKey;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuildingDto extends BaseDto {
    private BuildingKey key;
    private String label;
    private Integer maxLevel;
}

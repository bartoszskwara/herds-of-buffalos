package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BuildingNextLevelDto extends BaseDto {
    private BuildingDto building;
    private Integer currentLevel;
    private Integer nextLevel;
    private ResourcesDto cost;
    private List<BuildingDto> requiredBuildings;
    private Boolean requirementsMet;
}

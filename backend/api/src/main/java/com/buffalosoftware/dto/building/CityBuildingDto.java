package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CityBuildingDto extends BaseDto {
    private BuildingDto building;
    private Integer currentLevel;
    private Integer maxLevel;
    private BuildingNextLevelDto nextLevel;
    private Boolean enabled;

    @Getter
    @Builder
    public static class BuildingNextLevelDto extends BaseDto {
        private Integer level;
        private boolean upgradeRequirementsMet;
        private ResourcesDto upgradingCost;
        private Long upgradingTime;
    }
}

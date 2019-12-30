package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.unit.UnitSkills;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UnitRecruitmentDto extends BaseDto {
    private CityUnitDto unit;
    private Integer maxLevel;
    private List<UnitLevelDataDto> levelsData;

    @Getter
    @Builder
    public static class UnitLevelDataDto extends BaseDto {
        private Integer level;
        private Integer amountInCity;
        private UnitSkills skills;
        private ResourcesDto recruitmentCost;
        private boolean enabled;
        private ResourcesDto upgradingCost;
        private Boolean upgradeRequirementsMet;
    }
}

package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.unit.UnitSkills;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UnitUpgradeDto extends BaseDto {
    private CityUnitDto unit;
    private Integer maxLevel;
    private boolean enabled;
    private UnitSkills firstLevelSkills;
    private ResourcesDto firstLevelRecruitmentCost;
    private ResourcesDto upgradingCost;
    private Boolean upgradeRequirementsMet;
}

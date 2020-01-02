package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.unit.UnitSkills;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UnitLevelDataDto extends BaseDto {
    private Integer level;
    private Integer amountInCity;
    private UnitSkills skills;
    private ResourcesDto recruitmentCost;
    private Boolean enabled;
    private ResourcesDto upgradingCost;
    private Boolean upgradeRequirementsMet;
    private Integer maxToRecruit;
}


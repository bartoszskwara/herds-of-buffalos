package com.buffalosoftware.dto.unit;

import com.buffalosoftware.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UnitWithLevelsDto extends BaseDto {
    private CityUnitDto unit;
    private Integer maxLevel;
    private List<UnitLevelDataDto> levelsData;
}

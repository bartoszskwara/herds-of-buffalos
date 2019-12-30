package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CityUnitDto extends BaseDto {
    private String key;
    private String label;
    private String building;
    private Integer amount;
    private Integer level;
}

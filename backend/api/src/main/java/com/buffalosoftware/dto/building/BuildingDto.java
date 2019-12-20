package com.buffalosoftware.dto.building;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuildingDto extends BaseDto {
    private Long id;
    private String name;
    private Integer maxLevel;
}

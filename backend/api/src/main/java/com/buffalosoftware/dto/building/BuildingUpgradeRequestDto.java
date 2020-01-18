package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BuildingUpgradeRequestDto extends BaseDto {
    @NotBlank
    private String building;

    @Min(0)
    private Integer level;
}

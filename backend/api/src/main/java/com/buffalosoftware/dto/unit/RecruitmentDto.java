package com.buffalosoftware.dto.unit;

import com.buffalosoftware.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RecruitmentDto extends BaseDto {
    @NotBlank
    private String unit;

    @Min(0)
    private Integer amount;

    @Min(0)
    private Integer level;
}

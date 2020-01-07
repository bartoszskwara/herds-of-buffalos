package com.buffalosoftware.dto.unit;

import com.buffalosoftware.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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

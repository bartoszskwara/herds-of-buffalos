package com.buffalosoftware.dto.unit;

import com.buffalosoftware.dto.ProgressTaskDto;
import com.buffalosoftware.dto.ProgressTaskType;
import com.buffalosoftware.unit.Unit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitmentProgressDto extends ProgressTaskDto {
    private Unit unit;
    private Integer unitLevel;
    private String label;
    private Integer amount;
    private Long endDate;

    @Builder
    public RecruitmentProgressDto(Long id, ProgressTaskType type, Long creationDate, Long startDate, Long taskDuration, String status,
                                  Unit unit, Integer unitLevel, String label, Integer amount, Long endDate) {
        super(id, type, creationDate, startDate, taskDuration, status);
        this.unit = unit;
        this.unitLevel = unitLevel;
        this.label = label;
        this.amount = amount;
        this.endDate = endDate;
    }
}

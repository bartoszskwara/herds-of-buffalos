package com.buffalosoftware.dto.unit;

import com.buffalosoftware.dto.ProgressTaskDto;
import com.buffalosoftware.dto.ProgressTaskType;
import com.buffalosoftware.unit.Unit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PromotionProgressDto extends ProgressTaskDto {
    private Unit unit;
    private Integer level;
    private String label;

    @Builder
    public PromotionProgressDto(Long id, ProgressTaskType type, Long creationDate, Long startDate, Long taskDuration, String status,
                                Unit unit, Integer level, String label) {
        super(id, type, creationDate, startDate, taskDuration, status);
        this.unit = unit;
        this.level = level;
        this.label = label;
    }
}

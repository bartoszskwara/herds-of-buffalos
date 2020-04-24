package com.buffalosoftware.dto.building;

import com.buffalosoftware.dto.ProgressTaskDto;
import com.buffalosoftware.dto.ProgressTaskType;
import com.buffalosoftware.entity.Building;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ConstructionProgressDto extends ProgressTaskDto {
    private Building building;
    private String label;
    private Integer currentLevel;
    private Integer nextLevel;
    private Integer progress;
    private Long endDate;

    @Builder
    public ConstructionProgressDto(Long id, ProgressTaskType type, Long creationDate, Long startDate, Long taskDuration, String status, Building building,
                                   String label, Integer currentLevel, Integer nextLevel, Integer progress, Long endDate) {
        super(id, type, creationDate, startDate, taskDuration, status);
        this.building = building;
        this.label = label;
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;
        this.progress = progress;
        this.endDate = endDate;
    }
}

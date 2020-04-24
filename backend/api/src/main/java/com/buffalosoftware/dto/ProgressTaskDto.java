package com.buffalosoftware.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public abstract class ProgressTaskDto extends BaseDto {
    private Long id;
    private ProgressTaskType type;
    private Long creationDate;
    private Long startDate;
    private Long taskDuration;
    @Setter //TODO: TEMP
    private String status;
}

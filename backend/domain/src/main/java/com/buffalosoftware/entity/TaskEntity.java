package com.buffalosoftware.entity;

import com.buffalosoftware.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class TaskEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    protected LocalDateTime updateDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    public void start() {
        status = TaskStatus.InProgress;
        startDate = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public void complete() {
        status = TaskStatus.Completed;
        updateDate = LocalDateTime.now(ZoneId.of("UTC"));
    }
}

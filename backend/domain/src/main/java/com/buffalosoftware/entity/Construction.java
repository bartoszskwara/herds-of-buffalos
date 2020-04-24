package com.buffalosoftware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@NoArgsConstructor
@Table(schema = "public", name = "construction")
public class Construction extends TaskEntity {

    @Builder
    public Construction(TaskStatus status, LocalDateTime creationDate, LocalDateTime updateDate, LocalDateTime startDate,
                        Building building, Integer level, City city) {
        super(status, creationDate, updateDate, startDate);
        this.building = building;
        this.level = level;
        this.city = city;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "building", nullable = false)
    private Building building;

    @Column(name = "level", nullable = false)
    private Integer level;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

}

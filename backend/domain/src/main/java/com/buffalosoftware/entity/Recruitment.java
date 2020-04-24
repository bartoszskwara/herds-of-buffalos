package com.buffalosoftware.entity;

import com.buffalosoftware.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@NoArgsConstructor
@Table(schema = "public", name = "recruitment")
public class Recruitment extends TaskEntity {

    @Builder
    public Recruitment(TaskStatus status, LocalDateTime creationDate, LocalDateTime updateDate, LocalDateTime startDate,
                       Unit unit, Integer level, Integer amountLeft, Integer amount, CityBuilding cityBuilding) {
        super(status, creationDate, updateDate, startDate);
        this.unit = unit;
        this.level = level;
        this.amountLeft = amountLeft;
        this.amount = amount;
        this.cityBuilding = cityBuilding;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Unit unit;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Setter
    @Column(name = "amountLeft", nullable = false)
    private Integer amountLeft;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_building_id", nullable = false)
    private CityBuilding cityBuilding;

    public void decreaseAmount(int value) {
        if(value == 0) {
            return;
        }

        if(amountLeft - value <= 0) {
            amountLeft = 0;
        } else {
            amountLeft -= value;
        }
        updateDate = LocalDateTime.now(ZoneId.of("UTC"));
    }
}

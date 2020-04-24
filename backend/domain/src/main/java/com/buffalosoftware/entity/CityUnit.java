package com.buffalosoftware.entity;

import com.buffalosoftware.unit.Unit;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public", name = "city_unit")
@EqualsAndHashCode(callSuper = true)
public class CityUnit extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Setter
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "level", nullable = false)
    private Integer level;

    public void increaseAmount(int value) {
        amount += value;
    }
}

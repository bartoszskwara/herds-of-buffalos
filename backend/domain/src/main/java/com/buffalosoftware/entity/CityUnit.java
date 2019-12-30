package com.buffalosoftware.entity;

import com.buffalosoftware.unit.Unit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "public", name = "city_unit")
public class CityUnit extends BaseEntity {

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Unit unit;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Getter
    @Setter
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Getter
    @Setter
    @Column(name = "level", nullable = false)
    private Integer level;
}

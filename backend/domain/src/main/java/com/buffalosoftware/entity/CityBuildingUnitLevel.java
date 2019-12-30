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
@Table(schema = "public", name = "city_unit_level")
public class CityBuildingUnitLevel extends BaseEntity {

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Unit unit;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_building_id", nullable = false)
    private CityBuilding cityBuilding;

    @Getter
    @Setter
    @Column(name = "available_level", nullable = false)
    private Integer availableLevel;
}

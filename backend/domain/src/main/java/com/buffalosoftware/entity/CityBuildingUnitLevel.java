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
@Table(schema = "public", name = "city_unit_level")
@EqualsAndHashCode(callSuper = true)
public class CityBuildingUnitLevel extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_building_id", nullable = false)
    private CityBuilding cityBuilding;

    @Column(name = "available_level", nullable = false)
    private Integer availableLevel;
}

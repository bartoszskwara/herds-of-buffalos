package com.buffalosoftware.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "city_building")
public class CityBuilding extends BaseEntity {

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "building", nullable = false)
    private Building building;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Getter
    @Setter
    @Column(name = "level", nullable = false)
    private Integer level;

    @Getter
    @Setter
    @OneToMany(mappedBy = "cityBuilding")
    private Set<CityBuildingUnitLevel> unitLevels = new HashSet<>();
}

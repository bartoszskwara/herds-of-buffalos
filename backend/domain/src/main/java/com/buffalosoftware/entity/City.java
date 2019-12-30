package com.buffalosoftware.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "city")
public class City extends BaseEntity {

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Setter
    @Column(nullable = false, name = "name")
    private String name;

    @Getter
    @Setter
    @Column(nullable = false, name = "coords_x")
    private Long coordsX;

    @Getter
    @Setter
    @Column(nullable = false, name = "coords_y")
    private Long coordsY;

    @Getter
    @Setter
    @Column(nullable = false, name = "points")
    private Long points;

    @Getter
    @Setter
    @OneToMany(mappedBy = "city")
    private Set<CityBuilding> cityBuildings = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "city")
    private Set<CityResources> cityResources = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "city")
    private Set<CityUnit> cityUnits = new HashSet<>();
}



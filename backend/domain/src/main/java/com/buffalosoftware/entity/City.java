package com.buffalosoftware.entity;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public", name = "city")
@EqualsAndHashCode(callSuper = true)
public class City extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "coords_x")
    private Long coordsX;

    @Column(nullable = false, name = "coords_y")
    private Long coordsY;

    @Column(nullable = false, name = "points")
    private Long points;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<CityBuilding> cityBuildings = new HashSet<>();

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<CityResources> cityResources = new HashSet<>();

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<CityUnit> cityUnits = new HashSet<>();

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<Construction> constructions = new HashSet<>();
}



package com.buffalosoftware.entity;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public", name = "city_building")
@EqualsAndHashCode(callSuper = true)
public class CityBuilding extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "building", nullable = false)
    private Building building;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Builder.Default
    @OneToMany(mappedBy = "cityBuilding", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<CityBuildingUnitLevel> unitLevels = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "cityBuilding", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<Recruitment> recruitments = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "cityBuilding", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<Promotion> promotions = new HashSet<>();

    public void increaseLevel() {
        if(this.level + 1 > this.getBuilding().getMaxLevel()) {
            return;
        }
        this.level = this.level + 1;
        this.updateDate = LocalDateTime.now(ZoneId.of("UTC"));
    }
}

package com.buffalosoftware.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "public", name = "user_building")
public class UserBuilding extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "building", nullable = false)
    private BuildingKey building;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "level", nullable = false)
    private Integer level;

    public BuildingKey getBuilding() {
        return building;
    }

    public void setBuilding(BuildingKey building) {
        this.building = building;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}

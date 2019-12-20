package com.buffalosoftware.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "public", name = "building")
public class Building extends BaseEntity {

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "max_level")
    private Integer maxLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }
}

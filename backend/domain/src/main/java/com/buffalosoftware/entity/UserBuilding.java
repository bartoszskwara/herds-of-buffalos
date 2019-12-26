package com.buffalosoftware.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "public", name = "user_building")
public class UserBuilding extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "building", nullable = false)
    private Building building;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "level", nullable = false)
    private Integer level;
}

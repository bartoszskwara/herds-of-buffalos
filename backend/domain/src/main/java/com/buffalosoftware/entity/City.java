package com.buffalosoftware.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "public", name = "city")
public class City extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "coords_x")
    private Long coordsX;

    @Column(nullable = false, name = "coords_y")
    private Long coordsY;
}



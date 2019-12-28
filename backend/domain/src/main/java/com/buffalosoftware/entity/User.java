package com.buffalosoftware.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "user")
public class User extends BaseEntity {

    @Getter
    @Setter
    @Column(nullable = false, name = "name")
    private String name;

    @Getter
    @Setter
    @Column(nullable = false, name = "email")
    private String email;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user")
    private Set<City> cities = new HashSet<>();
}



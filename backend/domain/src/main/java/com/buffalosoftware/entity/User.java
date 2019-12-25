package com.buffalosoftware.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "user")
public class User extends BaseEntity {

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "points")
    private Long points;

    @OneToMany(mappedBy = "user")
    private Set<UserBuilding> userBuildings = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private UserResources userResources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserBuilding> getUserBuildings() {
        return userBuildings;
    }

    public void setUserBuildings(Set<UserBuilding> userBuildings) {
        this.userBuildings = userBuildings;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public UserResources getUserResources() {
        return userResources;
    }

    public void setUserResources(UserResources userResources) {
        this.userResources = userResources;
    }
}



package com.buffalosoftware.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "user")
    private Set<UserBuilding> userBuildings = new HashSet<>();

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
}



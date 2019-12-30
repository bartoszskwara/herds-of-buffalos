package com.buffalosoftware.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = "public", name = "city_resource")
public class CityResources extends BaseEntity {

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Getter
    @Setter
    @Column(name = "resource")
    @Enumerated(EnumType.STRING)
    private Resource resource;

    @Getter
    @Setter
    @Column(name = "amount")
    private Integer amount;

    @Getter
    @Setter
    @Column(name = "update_date")
    private Date updateDate;
}

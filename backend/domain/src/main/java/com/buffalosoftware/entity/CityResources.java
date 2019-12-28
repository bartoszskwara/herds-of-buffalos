package com.buffalosoftware.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(schema = "public", name = "city_resource")
public class CityResources extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "resource")
    @Enumerated(EnumType.STRING)
    private Resource resource;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "update_date")
    private Date updateDate;
}

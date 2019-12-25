package com.buffalosoftware.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(schema = "public", name = "user_resources")
public class UserResources extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "wood")
    private Long wood;

    @Column(name = "iron")
    private Long iron;

    @Column(name = "clay")
    private Long clay;

    @Column(name = "update_date")
    private Date updateDate;
}

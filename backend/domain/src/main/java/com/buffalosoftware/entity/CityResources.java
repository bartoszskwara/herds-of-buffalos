package com.buffalosoftware.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public", name = "city_resource")
@EqualsAndHashCode(callSuper = true)
public class CityResources extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "resource")
    @Enumerated(EnumType.STRING)
    private Resource resource;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public void decreaseAmount(int value) {
        if(value == 0) {
            return;
        }

        if(amount - value <= 0) {
            amount = 0;
        } else {
            amount -= value;
        }
        updateDate = LocalDateTime.now(ZoneId.of("UTC"));
    }
}

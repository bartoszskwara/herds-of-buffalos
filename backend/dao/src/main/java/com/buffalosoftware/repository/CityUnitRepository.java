package com.buffalosoftware.repository;

import com.buffalosoftware.entity.CityUnit;
import com.buffalosoftware.unit.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityUnitRepository extends JpaRepository<CityUnit, Long> {

    Optional<CityUnit> findByUnitAndCity_IdAndLevel(Unit unit, Long cityId, Integer level);
}

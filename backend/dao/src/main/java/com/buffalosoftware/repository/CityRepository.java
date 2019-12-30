package com.buffalosoftware.repository;

import com.buffalosoftware.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findCitiesByUser_Id(@Param("userId") Long userId);
}

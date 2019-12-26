package com.buffalosoftware.repository;

import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findCitiesByUser_Id(@Param("userId") Long userId);
}

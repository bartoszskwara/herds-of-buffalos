package com.buffalosoftware.repository;

import com.buffalosoftware.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findCitiesByUser_Id(@Param("userId") Long userId);
    Optional<City> findByIdAndUser_Id(@Param("cityId") Long cityId, @Param("userId") Long userId);

    @Query("select c from City c " +
            "left join fetch c.cityUnits cu " +
            "left join fetch c.cityBuildings cb " +
            "left join fetch c.constructions con " +
            "left join fetch cb.recruitments r ")
    List<City> findCitiesWithFetchedData();

    @Query("select c from City c " +
            "join fetch c.cityUnits cu")
    List<City> a();

    @Query("select c from City c " +
            "join fetch c.cityUnits cu " +
            "join fetch c.cityBuildings cb")
    List<City> b();

    @Query("select c from City c " +
            "join fetch c.cityUnits cu " +
            "join fetch c.cityBuildings cb " +
            "join fetch c.constructions con ")
    List<City> c();
}

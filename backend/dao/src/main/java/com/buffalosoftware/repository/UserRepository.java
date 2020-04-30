package com.buffalosoftware.repository;

import com.buffalosoftware.dto.UserPointsDto;
import com.buffalosoftware.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u left join fetch u.cities c left join fetch c.cityBuildings cb where u.id = :userId")
    Optional<User> findUserWithCitiesAndBuildingsById(@Param("userId") Long userId);

    @Query("select u from User u left join fetch u.cities c left join fetch c.cityBuildings cb left join fetch cb.unitLevels cu where u.id = :userId")
    Optional<User> findUserWithCitiesAndBuildingsAndUnitLevelsById(@Param("userId") Long userId);

    @Query("select u from User u left join fetch u.cities c left join fetch c.cityBuildings left join fetch c.cityResources where u.id = :userId")
    Optional<User> findUserWithCitiesAndBuildingsAndResourcesById(@Param("userId") Long userId);

    @Query("select u from User u left join fetch u.cities c left join fetch c.cityUnits where u.id = :userId")
    Optional<User> findUserWithCitiesAndCityUnitsById(@Param("userId") Long userId);

    @Query("select u from User u left join fetch u.cities c where u.id = :userId")
    Optional<User> findUserWithCitiesById(@Param("userId") Long userId);

    @Query(value = "select new com.buffalosoftware.dto.UserPointsDto(u.id, sum(c.points)) from User u " +
            "left join u.cities c " +
            "group by u.id")
    List<UserPointsDto> findUsersWithPoints();

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}

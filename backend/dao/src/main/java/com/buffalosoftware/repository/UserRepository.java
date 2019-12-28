package com.buffalosoftware.repository;

import com.buffalosoftware.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u join fetch u.cities c join fetch c.cityBuildings where u.id = :userId")
    Optional<User> findUserWithCitiesAndBuildingsById(@Param("userId") Long userId);

    @Query("select u from User u join fetch u.cities c join fetch c.cityBuildings join fetch c.cityResources where u.id = :userId")
    Optional<User> findUserWithCitiesAndBuildingsAndResourcesById(@Param("userId") Long userId);

    @Query("select u from User u join fetch u.cities c join fetch c.cityResources where u.id = :userId")
    Optional<User> findUserWithCitiesAndResourcesById(@Param("userId") Long userId);

    @Query(value = "select u.rank from " +
            "(select row_number() over (order by points desc) as rank, id " +
                "FROM (select us.id as id, sum(points) as points from user us left join city c on us.id = c.user_id group by us.id) ) u " +
                "where u.id = :userId",
            nativeQuery = true)
    Long findRankByPoints(@Param("userId") Long userId);
}

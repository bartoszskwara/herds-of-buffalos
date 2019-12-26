package com.buffalosoftware.repository;

import com.buffalosoftware.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u join fetch u.userBuildings where u.id = :userId")
    Optional<User> findUserWithBuildingsById(@Param("userId") Long userId);

    @Query("select u from User u join fetch u.userBuildings join fetch u.userResources where u.id = :userId")
    Optional<User> findUserWithBuildingsAndResourcesById(@Param("userId") Long userId);

    @Query(value = "select u.rank from (select row_number() over (order by points desc) as rank, id FROM USER) u where u.id = :userId",
            nativeQuery = true)
    Long findRankByPoints(@Param("userId") Long userId);
}

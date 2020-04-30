package com.buffalosoftware.repository;

import com.buffalosoftware.entity.Construction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstructionRepository extends JpaRepository<Construction, Long> {
}

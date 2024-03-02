package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.WorkoutEntity;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity,Long>,WorkoutRepositoryCustom {

    Optional<WorkoutEntity> findByUuidEntityUuid(UUID uuid);

    boolean existsByUuidEntityUuid(UUID uuid);
}

package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity,Long>,WorkoutRepositoryCustom {

    Optional<WorkoutEntity> findByUuidEntityUuid(UUID uuid);

    boolean existsByUuidEntityUuid(UUID uuid);
}

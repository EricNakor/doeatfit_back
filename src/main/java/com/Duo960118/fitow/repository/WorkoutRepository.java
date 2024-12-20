package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.WorkoutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity,Long>,WorkoutRepositoryCustom {

    Optional<WorkoutEntity> findByUuidEntityUuid(UUID uuid);

    boolean existsByUuidEntityUuid(UUID uuid);

    // 운동 이름 으로 찾기
    Page<WorkoutEntity> findAllByWorkoutNameContains(String keyword, Pageable pageable);

    // minor muscle로 운동 찾기
    Page<WorkoutEntity> findAllByAgonistMuscleEnumsIn(Collection<WorkoutEntity.MinorMuscleEnum> agonistMuscleEnums, Pageable pageable);
}

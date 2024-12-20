package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.WorkoutDto;
import com.Duo960118.fitow.entity.WorkoutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WorkoutRepositoryCustom {
    Slice<WorkoutEntity> findBySearchWorkoutRequest(WorkoutDto.SearchWorkoutRequestDto searchWorkoutRequest, Pageable pageable);
}

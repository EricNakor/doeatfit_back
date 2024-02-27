package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.WorkoutEntity;
import com.Duo960118.fitow.entity.WorkoutDto;
import org.springframework.context.annotation.Bean;

import java.util.*;

public class WorkoutMapper {
    @Bean
    public static WorkoutDto.WorkoutDetailDto entityToWorkoutDetailDto(WorkoutEntity workoutEntity){
        return new WorkoutDto.WorkoutDetailDto(workoutEntity.getUuidEntity().getUuid(),
                workoutEntity.getWorkoutName(),
                workoutEntity.getWorkoutDifficulty(),
                workoutEntity.getAgonistMuscleEnums(),
                workoutEntity.getAntagonistMuscleEnums(),
                workoutEntity.getSynergistMuscleEnums(),
                workoutEntity.getDescriptions(), workoutEntity.getMediaFileName());
    }
}

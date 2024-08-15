package com.Duo960118.fitow.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WorkoutDto {

    // 운동 상세 정보
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkoutDetailDto {
        private UUID uuid;
        private String workoutName;
        private WorkoutEntity.DifficultyEnum workoutDifficulty;
        private Set<WorkoutEntity.MuscleEnum> agonistMuscles;
        private Set<WorkoutEntity.MuscleEnum> antagonistMuscles;
        private Set<WorkoutEntity.MuscleEnum> synergistMuscles;
        private List<String> descriptions;
        private String mediaFileName;
    }

    // 운동 작성 및 수정 요청 dto
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PostWorkoutRequestDto {
        private String workoutName;
        private WorkoutEntity.DifficultyEnum workoutDifficulty;
        private List<WorkoutEntity.MuscleEnum> agonistMuscles;
        private List<WorkoutEntity.MuscleEnum> antagonistMuscles;
        private List<WorkoutEntity.MuscleEnum> synergistMuscles;
        private List<String> descriptions;
        private String mediaFileName;
        private UUID uuid;
        private MultipartFile workoutFile;

        // 왜 빌더를 이렇게 해놓았을까?
        @Builder
        public PostWorkoutRequestDto(String workoutName,
                                     WorkoutEntity.DifficultyEnum workoutDifficulty,
                                     List<WorkoutEntity.MuscleEnum> agonistMuscles,
                                     List<WorkoutEntity.MuscleEnum> antagonistMuscles,
                                     List<WorkoutEntity.MuscleEnum> synergistMuscles,
                                     List<String> descriptions,
                                     String mediaFileName,
                                     UUID uuid,
                                     MultipartFile workoutFile) {
            this.workoutName = workoutName;
            this.workoutDifficulty = workoutDifficulty;
            this.agonistMuscles = agonistMuscles;
            this.antagonistMuscles = antagonistMuscles;
            this.synergistMuscles = synergistMuscles;
            this.descriptions = descriptions;
            this.mediaFileName = mediaFileName;
            this.uuid = uuid;
            this.workoutFile = workoutFile;
        }
    }

//    // 운동 작성 응답 dto
//    @Getter
//    @RequiredArgsConstructor
//    @Builder
//    public static class PostWorkoutResponseDto {
//        private final UUID uuid;
//        private final String workoutName;
//        private final WorkoutEntity.DifficultyEnum workoutDifficulty;
//        private final Set<WorkoutEntity.MuscleEnum> agonistMuscles;
//        private final Set<WorkoutEntity.MuscleEnum> antagonistMuscles;
//        private final Set<WorkoutEntity.MuscleEnum> synergistMuscles;
//        private final List<String> descriptions;
//        private final String mediaFileName;
//    }

    // 운동 검색 요청 dto
    @NoArgsConstructor
    @Getter
    public static class SearchWorkoutRequestDto {
        private List<WorkoutEntity.DifficultyEnum> workoutDifficulties;
        private String workoutName;
        private List<WorkoutEntity.BodyPartEnum> bodyPartEnums;
        private List<WorkoutEntity.MuscleEnum> agonistMuscleEnums;
        private List<WorkoutEntity.MuscleEnum> antagonistMuscleEnums;
        private List<WorkoutEntity.MuscleEnum> synergistMuscleEnums;

        @Builder
        public SearchWorkoutRequestDto(
                List<WorkoutEntity.DifficultyEnum> workoutDifficulties,
                String workoutName,
                List<WorkoutEntity.BodyPartEnum> bodyPartEnums,
                List<WorkoutEntity.MuscleEnum> agonistMuscleEnums,
                List<WorkoutEntity.MuscleEnum> antagonistMuscleEnums,
                List<WorkoutEntity.MuscleEnum> synergistMuscleEnums
        ) {
            this.workoutDifficulties = workoutDifficulties;
            this.workoutName = workoutName;
            this.bodyPartEnums = bodyPartEnums;
            this.agonistMuscleEnums = agonistMuscleEnums;
            this.antagonistMuscleEnums = antagonistMuscleEnums;
            this.synergistMuscleEnums = synergistMuscleEnums;
        }
    }
}

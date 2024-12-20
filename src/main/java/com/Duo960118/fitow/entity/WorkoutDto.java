package com.Duo960118.fitow.entity;

import com.Duo960118.fitow.annotaion.Enum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.domain.Pageable;
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
        private Set<WorkoutEntity.MinorMuscleEnum> agonistMuscles;
        private Set<WorkoutEntity.MinorMuscleEnum> antagonistMuscles;
        private Set<WorkoutEntity.MinorMuscleEnum> synergistMuscles;
        private List<String> descriptions;
        private String mediaFileName;
    }

    // 운동 작성 요청 dto
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PostWorkoutRequestDto {
        @Size(min=2, max=30)
        @NotBlank(message = "{NotBlank.workoutName}")
        private String workoutName;
        @Enum(enumClass = WorkoutEntity.DifficultyEnum.class, message = "올바르지 않은 운동 난이도 enum")
        @NotBlank(message = "{NotBlank.workoutDifficulty}")
        private String workoutDifficulty;
        @NotEmpty(message = "{NotEmpty.agonistMuscles}")
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class,message = "{Enum.agonistMuscles}") String> agonistMuscles;
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class,message = "{Enum.antagonistMuscles}") String> antagonistMuscles;
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class,message = "{Enum.synergistMuscles}") String> synergistMuscles;
        @NotEmpty(message = "{NotEmpty.descriptions}")
        private List<@NotBlank(message = "{NotBlank.description}") String> descriptions;
        private String mediaFileName;
//        @File(allowedFileExt = {"mp4"}, fileSizeLimit = 1024 * 1024 * 50)
        private MultipartFile mediaFile;

        // 왜 빌더를 이렇게 해놓았을까?
        @Builder
        public PostWorkoutRequestDto(String workoutName,
                                     String workoutDifficulty,
                                     List<String> agonistMuscles,
                                     List<String> antagonistMuscles,
                                     List<String> synergistMuscles,
                                     List<String> descriptions,
                                     String mediaFileName,
                                     MultipartFile mediaFile) {
            this.workoutName = workoutName;
            this.workoutDifficulty = workoutDifficulty;
            this.agonistMuscles = agonistMuscles;
            this.antagonistMuscles = antagonistMuscles;
            this.synergistMuscles = synergistMuscles;
            this.descriptions = descriptions;
            this.mediaFileName = mediaFileName;
            this.mediaFile = mediaFile;
        }
    }

    // 운동 수정 요청 dto
    @NoArgsConstructor
    @Getter
    @Setter
    public static class EditWorkoutRequestDto {
        @Size(min=2, max=30)
        @NotBlank(message = "{NotBlank.workoutName}")
        private String workoutName;
        @Enum(enumClass = WorkoutEntity.DifficultyEnum.class, message = "{Enum.workoutDifficulty}")
        @NotBlank(message = "{NotBlank.workoutDifficulty}")
        private String workoutDifficulty;
        @NotEmpty(message = "{NotEmpty.agonistMuscles}")
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class, message = "{Enum.agonistMuscles}") String> agonistMuscles;
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class, message = "{Enum.antagonistMuscles}") String> antagonistMuscles;
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class, message = "{Enum.synergistMuscles}") String> synergistMuscles;
        @NotEmpty(message = "{NotEmpty.descriptions}")
        private List<@NotBlank(message = "{NotBlank.description}") String> descriptions;
        private String mediaFileName;
//        @File(allowedFileExt = {"mp4"}, fileSizeLimit = 1024 * 1024 * 50)
        private MultipartFile workoutFile;
        private UUID uuid;

        // 왜 빌더를 이렇게 해놓았을까?
        @Builder
        public EditWorkoutRequestDto(String workoutName,
                                     String workoutDifficulty,
                                     List<String> agonistMuscles,
                                     List<String> antagonistMuscles,
                                     List<String> synergistMuscles,
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


    // 운동 검색 요청 dto
    @NoArgsConstructor
    @Getter
    public static class SearchWorkoutRequestDto {
        private List<@Enum(enumClass = WorkoutEntity.DifficultyEnum.class,message = "{Enum.workoutDifficulty}") String> workoutDifficulties;
        private String workoutName;
        private List<@Enum(enumClass = WorkoutEntity.MajorMuscleEnum.class,message = "{Enum.majorMuscleEnum}")String> majorMuscles;
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class,message = "{Enum.agonistMuscles}")String> agonistMuscles;
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class,message = "{Enum.antagonistMuscles}") String> antagonistMuscles;
        private List<@Enum(enumClass = WorkoutEntity.MinorMuscleEnum.class,message = "{Enum.synergistMuscles}")String> synergistMuscles;

        @Builder
        public SearchWorkoutRequestDto(
                List<String> workoutDifficulties,
                String workoutName,
                List<String> majorMuscles,
                List<String> agonistMuscles,
                List<String> antagonistMuscles,
                List<String> synergistMuscles
        ) {
            this.workoutDifficulties = workoutDifficulties;
            this.workoutName = workoutName;
            this.majorMuscles = majorMuscles;
            this.agonistMuscles = agonistMuscles;
            this.antagonistMuscles = antagonistMuscles;
            this.synergistMuscles = synergistMuscles;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class SearchWorkoutKeywordRequestDto {
        private final String keyword;
        private final Pageable pageable;
    }
}

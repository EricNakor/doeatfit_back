//package com.Duo960118.fitow.entity;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.databind.ser.std.StdSerializer;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.*;
//
//@Getter
//@NoArgsConstructor
//@Entity
//@Table(name = "WORKOUT")
//public class WorkoutEntity extends TimeStampEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(updatable = false)
//    private long workoutId;
//
//    @Embedded
//    private UuidEntity uuidEntity;
//
//    // 운동 이름
//    @Column(unique = true)
//    private String workoutName;
//
//    // 운동 난이도
//    private DifficultyEnum workoutDifficulty;
//
//    // JPA에서는 @Convert 어노테이션을 Entity의 프로퍼티에 적용하면,
//    // @Converter 어노테이션이 적용된 클래스와 자동으로 매핑하여,
//    // DB에 저장된 varChar 데이터타입을 Enum 으로 매핑시켜,
//    // Java에서 Enum 클래스로 핸들링 할 수 있도록 해줍니다.
//
//    // 주동근
//    @ElementCollection
//    @CollectionTable(name = "AGONIST_MUSCLE_ENUMS", joinColumns =
//    @JoinColumn(name = "WORKOUT_ID"))
//    private Set<MuscleEnum> agonistMuscleEnums;
//
//    // 길항근
//    @ElementCollection
//    @CollectionTable(name = "ANTAGONIST_MUSCLE_ENUMS", joinColumns =
//    @JoinColumn(name = "WORKOUT_ID"))
//    private Set<MuscleEnum> antagonistMuscleEnums;
//
//    // 협응근
//    @ElementCollection
//    @CollectionTable(name = "SYNERGIST_MUSCLE_ENUMS", joinColumns =
//    @JoinColumn(name = "WORKOUT_ID"))
//    private Set<MuscleEnum> synergistMuscleEnums;
//
//    // 운동 설명
//    @ElementCollection
//    @CollectionTable(name = "DESCRIPTIONS", joinColumns =
//    @JoinColumn(name = "WORKOUT_ID"))
//    private List<String> descriptions;
//
//    // 영상 경로 or 외부 영상 링크
//    private String mediaFileName;
//
//    @Builder
//    public WorkoutEntity(String workoutName, DifficultyEnum workoutDifficulty, Set<MuscleEnum> agonistMuscle, Set<MuscleEnum> antagonistMuscle, Set<MuscleEnum> synergistMuscle, List<String> descriptions, String mediaFileName) {
//        this.workoutName = workoutName;
//        this.workoutDifficulty = workoutDifficulty;
//        this.agonistMuscleEnums = agonistMuscle;
//        this.antagonistMuscleEnums = antagonistMuscle;
//        this.synergistMuscleEnums = synergistMuscle;
//        this.descriptions = descriptions;
//        this.mediaFileName = mediaFileName;
//    }
//
//    public void updateWorkoutEntity(WorkoutDto.PostWorkoutRequestDto editWorkoutRequest) {
//        this.workoutName = editWorkoutRequest.getWorkoutName();
//        this.workoutDifficulty = editWorkoutRequest.getWorkoutDifficulty();
//        this.agonistMuscleEnums = new HashSet<>(editWorkoutRequest.getAgonistMuscles());
//        this.antagonistMuscleEnums = new HashSet<>(editWorkoutRequest.getAntagonistMuscles());
//        this.synergistMuscleEnums = new HashSet<>(editWorkoutRequest.getSynergistMuscles());
//        this.descriptions = editWorkoutRequest.getDescriptions();
//        this.mediaFileName = editWorkoutRequest.getMediaFileName();
//    }
//
//    @Getter
//    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
//    public enum MuscleEnum {
//        // 광배근
//        LATISSIMUS_DORSI_MUSCLE("LATISSIMUS_DORSI_MUSCLE", "광배근", BodyPartEnum.BACK),
//
//        // 승모근
//        TRAPEZIUS("TRAPEZIUS", "승모근", BodyPartEnum.BACK),
//
//        // 기립근
//        ERECTOR_MUSCLE("ERECTOR_MUSCLE", "기립근", BodyPartEnum.BACK),
//
//        // 대퇴 사두근
//        QUADRICEPS_FEMORIS_MUSCLE("QUADRICEPS_FEMORIS_MUSCLE", "대퇴 사두근", BodyPartEnum.LEG),
//        // 햄스트링 ( 대퇴 이두근 + 반건양근, 반막양근 )
//        HAMSTRING("HAMSTRING", "햄스트링", BodyPartEnum.LEG),
//        // 대둔근
//        GLUTEUS_MAXIMUS_MUSCLE("GLUTEUS_MAXIMUS_MUSCLE", "대둔근", BodyPartEnum.LEG),
//        // 이두근
//        BICEPS_MUSCLE("BICEPS_MUSCLE", "이두근", BodyPartEnum.ARM),
//        // 삼두근
//        TRICEPS_MUSCLE("TRICEPS_MUSCLE", "삼두근", BodyPartEnum.ARM);
//
//        private final String name;
//        private final String desc;
//        private final BodyPartEnum bodyPart;
//
//        MuscleEnum(String name, String desc, BodyPartEnum bodyPart) {
//            this.name = name;
//            this.desc = desc;
//            this.bodyPart = bodyPart;
//        }
//
//        // mapper 사용 안할려고
//        // @JsonCreator
//        // Json 을 Enum 타입으로 변환
//        @JsonCreator
//        public static MuscleEnum fromString(String name
////                                            ,@JsonProperty("desc") String desc,
////                                          @JsonProperty("bodyPart") BodyPartEnum bodyPart
//        ) {
//            for (MuscleEnum muscleEnum : MuscleEnum.values()) {
//                if (muscleEnum.name.equals(name)) {
//                    return muscleEnum;
//                }
//            }
//            return null;
//        }
//
//        // String to Enum
////        @JsonCreator
////        public static MuscleEnum fromString(String name) {
////            return MuscleEnum.valueOf(name);
////        }
//
//        // desc에 맞는 MuscleEnum 찾기
//        public static Optional<MuscleEnum> findByDesc(String desc) {
//            return Arrays.stream(MuscleEnum.values()).
//                    filter(muscle -> muscle.getDesc().equals(desc)).findFirst();
//        }
//    }
//
//    @Getter
//    public enum BodyPartEnum {
//        ARM("팔"),
//        BACK("등"),
//        ABDOMEN("복부"),
//        LEG("다리"),
//        CHEST("가슴"),
//        SHOULDER("어깨"),
//        HIP("엉덩이");
//
//        private final String desc;
//
//        BodyPartEnum(String desc) {
//            this.desc = desc;
//        }
//    }
//
//    //    @Getter
////    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
////    public enum DifficultyEnum {
////        BEGINNER("BEGINNER", "초급"),
////        INTERMEDIATE("INTERMEDIATE", "중급"),
////        ADVANCED("ADVANCED", "고급");
////
////        private final String name;
////        private final String desc;
////
////        DifficultyEnum(String name, String desc) {
////            this.name = name;
////            this.desc = desc;
////        }
////
////        // mapper 사용 안할려고
////        // @JsonCreator
////        // Json to Enum
////        @JsonCreator
////        public static DifficultyEnum fromJson(@JsonProperty("workoutDifficulty") String name
//////                                              ,@JsonProperty("desc") String desc
////        ) {
////            for (DifficultyEnum difficultyEnum : DifficultyEnum.values()) {
////                if (difficultyEnum.name.equals(name)) {
////                    return difficultyEnum;
////                }
////            }
////            return null;
////        }
////
////        // String to Enum
////        @JsonCreator
////        public static DifficultyEnum fromString(String name) {
////            return DifficultyEnum.valueOf(name);
////        }
////    }
//    @Getter
//    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
//    public enum DifficultyEnum {
//        BEGINNER("BEGINNER", "초급"),
//        INTERMEDIATE("INTERMEDIATE", "중급"),
//        ADVANCED("ADVANCED", "고급");
//
//        private final String name;
//        private final String desc;
//
//        DifficultyEnum(String name, String desc) {
//            this.name = name;
//            this.desc = desc;
//        }
//
//        // String to Enum
//        @JsonCreator
//        public static DifficultyEnum fromString(String name) {
//            for (DifficultyEnum difficultyEnum : DifficultyEnum.values()) {
//                if (difficultyEnum.name.equals(name)) {
//                    return difficultyEnum;
//                }
//            }
//            throw new IllegalArgumentException("Invalid difficulty: " + name);
//        }
//    }
//}
package com.Duo960118.fitow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "WORKOUT")
public class WorkoutEntity extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long workoutId;

    @Embedded
    private UuidEntity uuidEntity;

    // 운동 이름
    @Column(unique = true)
    @Size(min=2, max=30,message = "{Size.workoutName}")
    @NotBlank(message="{NotBlank.workoutName}")
    private String workoutName;

    // 운동 난이도
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{NotNull.workoutDifficulty}")
    private DifficultyEnum workoutDifficulty;

    // 주동근
    @ElementCollection
    @CollectionTable(name = "AGONIST_MUSCLE_ENUMS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "{NotEmpty.agonistMuscleEnums}")
    private Set<MinorMuscleEnum> agonistMuscleEnums;
    // 허용되는 범위
    // NotNull :  {}, {muscle_1,....}
    // NotEmpty : {muscle_1,...}

    // 길항근
    @ElementCollection
    @CollectionTable(name = "ANTAGONIST_MUSCLE_ENUMS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
    @Enumerated(EnumType.STRING)
    private Set<MinorMuscleEnum> antagonistMuscleEnums;

    // 협응근
    @ElementCollection
    @CollectionTable(name = "SYNERGIST_MUSCLE_ENUMS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
    @Enumerated(EnumType.STRING)
    private Set<MinorMuscleEnum> synergistMuscleEnums;

    // 운동 설명
    @ElementCollection
    @CollectionTable(name = "DESCRIPTIONS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
    @NotEmpty(message = "{NotEmpty.descriptions}")
    private List<@NotBlank(message = "{NotBlank.description}") String> descriptions;

    // 영상 경로 or 외부 영상 링크
    @NotBlank(message = "{NotBlank.mediaFileName}")
    private String mediaFileName;

    @Builder
    public WorkoutEntity(String workoutName, DifficultyEnum workoutDifficulty, Set<MinorMuscleEnum> agonistMuscle, Set<MinorMuscleEnum> antagonistMuscle, Set<MinorMuscleEnum> synergistMuscle, List<String> descriptions, String mediaFileName) {
        this.workoutName = workoutName;
        this.workoutDifficulty = workoutDifficulty;
        this.agonistMuscleEnums = agonistMuscle;
        this.antagonistMuscleEnums = antagonistMuscle;
        this.synergistMuscleEnums = synergistMuscle;
        this.descriptions = descriptions;
        this.mediaFileName = mediaFileName;
    }

    public void updateWorkoutEntity(WorkoutDto.EditWorkoutRequestDto editWorkoutRequest) {
        this.workoutName = editWorkoutRequest.getWorkoutName();
        this.workoutDifficulty = DifficultyEnum.valueOf(editWorkoutRequest.getWorkoutDifficulty());
        this.agonistMuscleEnums = editWorkoutRequest.getAgonistMuscles().stream().map(MinorMuscleEnum::fromString).collect(Collectors.toSet());
        this.antagonistMuscleEnums = editWorkoutRequest.getAntagonistMuscles().stream().map(MinorMuscleEnum::fromString).collect(Collectors.toSet());
        this.synergistMuscleEnums = editWorkoutRequest.getSynergistMuscles().stream().map(MinorMuscleEnum::fromString).collect(Collectors.toSet());
        this.descriptions = editWorkoutRequest.getDescriptions();
        this.mediaFileName = editWorkoutRequest.getMediaFileName();
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum MinorMuscleEnum {
        // 등
//        TRAPEZIUS("TRAPEZIUS", "승모근", BodyPartEnum.BACK),
        UPPER_TRAPEZIUS("UPPER_TRAPEZIUS", "상부 승모근", MajorMuscleEnum.BACK),
        MIDDLE_TRAPEZIUS("MIDDLE_TRAPEZIUS", "중부 승모근", MajorMuscleEnum.BACK),
        LOWER_TRAPEZIUS("LOWER_TRAPEZIUS", "하부 승모근", MajorMuscleEnum.BACK),

//        LATISSIMUS_DORSI("LATISSIMUS_DORSI", "광배근", BodyPartEnum.BACK),
        UPPER_LATISSIMUS_DORSI("UPPER_LATISSIMUS_DORSI", "상부 광배근", MajorMuscleEnum.BACK),
        LOWER_LATISSIMUS_DORSI("LOWER_LATISSIMUS_DORSI", "하부 광배근", MajorMuscleEnum.BACK),

        TERES_MAJOR("TERES_MAJOR", "대원근", MajorMuscleEnum.BACK),
        TERES_MINOR("TERES_MINOR", "소원근", MajorMuscleEnum.BACK),
        LEVATOR_SCAPULA("LEVATOR_SCAPULA", "견갑거근", MajorMuscleEnum.BACK),

        RHOMBOIDS("RHOMBOIDS", "능형근", MajorMuscleEnum.BACK),
        MAJOR_RHOMBOIDS("MAJOR_RHOMBOIDS", "대능형근", MajorMuscleEnum.BACK),
        MINOR_RHOMBOIDS("MINOR_RHOMBOIDS", "소능형근", MajorMuscleEnum.BACK),

        ERECTOR_MUSCLE("ERECTOR_MUSCLE", "기립근", MajorMuscleEnum.BACK),

        // 어깨
        //        DELTOID("DELTOID", "삼각근", BodyPartEnum.SHOULDER),
        ANTERIOR_DELTOID("ANTERIOR_DELTOID", "전면 삼각근", MajorMuscleEnum.SHOULDER),
        LATERAL_DELTOID("LATERAL_DELTOID", "측면 삼각근", MajorMuscleEnum.SHOULDER),
        POSTERIOR_DELTOID("POSTERIOR_DELTOID", "후면 삼각근", MajorMuscleEnum.SHOULDER),

        CORACOBRACHIALIS("CORACOBRACHIALIS", "오훼완근", MajorMuscleEnum.SHOULDER),

        SUPRASPINATUS("SUPRASPINATUS", "극상근", MajorMuscleEnum.SHOULDER),
        INFRASPINATUS("INFRASPINATUS", "극하근", MajorMuscleEnum.SHOULDER),
        SUBSCAPULARIS("SUBSCAPULARIS", "견갑하근", MajorMuscleEnum.SHOULDER),

        // 가슴
//        PECTORALIS_MAJOR("PECTORALIS_MAJOR","대흉근", MajorMuscleEnum.CHEST),
        UPPER_PECTORALIS_MAJOR("UPPER_PECTORALIS_MAJOR","상부 대흉근", MajorMuscleEnum.CHEST),
        MIDDLE_PECTORALIS_MAJOR("MIDDLE_PECTORALIS_MAJOR","중부 대흉근", MajorMuscleEnum.CHEST),
        LOWER_PECTORALIS_MAJOR("LOWER_PECTORALIS_MAJOR","하부 대흉근", MajorMuscleEnum.CHEST),

        PECTORALIS_MINOR("PECTORALIS_MINOR","소흉근", MajorMuscleEnum.CHEST),

        SUBCLAVIUS("SUBCLAVIUS","쇄골하근", MajorMuscleEnum.CHEST),

        SERRATUS_ANTERIOR("SERRATUS_ANTERIOR","전거근", MajorMuscleEnum.SERRATUS_ANTERIOR),

        // 팔
//        BICEPS("BICEPS", "이두근", BodyPartEnum.ARM),
        BICEPS_LONG_HEAD("BICEPS_LONG_HEAD","상완 이두 장두", MajorMuscleEnum.BICEPS),
        BICEPS_SHORT_HEAD("BICEPS_SHORT_HEAD","상완 이두 단두", MajorMuscleEnum.BICEPS),
//        BRACHIALIS("BRACHIALIS","상완근", BodyPartEnum.ARM),

//        TRICEPS("TRICEPS", "삼두근", BodyPartEnum.ARM),
        TRICEPS_LONG_HEAD("TRICEPS_LONG_HEAD","상완 삼두 장두", MajorMuscleEnum.TRICEPS),
        TRICEPS_LATERAL("TRICEPS_LATERAL","상완 삼두 외측", MajorMuscleEnum.TRICEPS),
        TRICEPS_MEDIAL_HEAD("TRICEPS_MEDIAL_HEAD","상완 삼두 내측", MajorMuscleEnum.TRICEPS),

//        ANTEBRACHIAL("ANTEBRACHIAL","전완근", BodyPartEnum.ARM),
        WRIST_EXTENSORS("WRIST_EXTENSORS","전완 신근", MajorMuscleEnum.ANTEBRACHIAL),
        WRIST_FLEXORS("WRIST_FLEXORS","전완 굴근", MajorMuscleEnum.ANTEBRACHIAL),

        // 복근
        UPPER_RECTUS_ABDOMINIS("UPPER_RECTUS_ABDOMINIS","상복직근", MajorMuscleEnum.ABDOMEN),
        LOWER_RECTUS_ABDOMINIS("LOWER_RECTUS_ABDOMINIS","하복직근", MajorMuscleEnum.ABDOMEN),

        EXTERNAL_OBLIQUE("EXTERNAL_OBLIQUE","외복사근", MajorMuscleEnum.ABDOMEN),

        INTERNAL_OBLIQUE("INTERNAL_OBLIQUE","내복사근", MajorMuscleEnum.ABDOMEN),
        TRANSVERSE_OBLIQUE("TRANSVERSE_OBLIQUE","복횡근", MajorMuscleEnum.ABDOMEN),
        QUADRATUS_LUMBORUM("QUADRATUS_LUMBORUM","요방형근", MajorMuscleEnum.ABDOMEN),

        // 둔근
//        GLUTEUS("GLUTEUS", "둔근", BodyPartEnum.HIP),
        GLUTEUS_MAXIMUS("GLUTEUS_MAXIMUS", "대둔근", MajorMuscleEnum.HIP),
        GLUTEUS_MEDIUS("GLUTEUS_MEDIUS", "중둔근", MajorMuscleEnum.HIP),
        GLUTEUS_MINIMUS("GLUTEUS_MINIMUS", "소둔근", MajorMuscleEnum.HIP),

        LATERAL_ROTATORS_HIP_JOINT("LATERAL_ROTATORS_HIP_JOINT", "고관절 외회전근", MajorMuscleEnum.HIP),
        PSOAS_MAJOR("PSOAS_MAJOR", "대요근", MajorMuscleEnum.HIP),
        ILIACUS("ILIACUS", "장골근", MajorMuscleEnum.HIP),
        PECTINEUS("PECTINEUS", "치골근", MajorMuscleEnum.HIP),

        // 하체
//        QUADRICEPS_FEMORIS("QUADRICEPS_FEMORIS", "대퇴 사두근", BodyPartEnum.LEG),
        RECTUS_FEMORIS("RECTUS_FEMORIS", "대퇴직근", MajorMuscleEnum.THIGH),
        VASTUS_MEDIALIS("VASTUS_MEDIALIS", "내측광근", MajorMuscleEnum.THIGH),
        VASTUS_LATERALIS("VASTUS_LATERALIS", "외측광근", MajorMuscleEnum.THIGH),

        TENSOR_FASCIAE_LATAE("TENSOR_FASCIAE_LATAE", "대퇴근막장근", MajorMuscleEnum.THIGH),

        ADDUCTOR("ADDUCTOR", "내전근", MajorMuscleEnum.THIGH),
        ADDUCTOR_MAGNUS("ADDUCTOR_MAGNUS", "대내전근", MajorMuscleEnum.THIGH),
        ADDUCTOR_LONGUS("ADDUCTOR_LONGUS", "장내전근", MajorMuscleEnum.THIGH),
        ADDUCTOR_BREVIS("ADDUCTOR_BREVIS", "단내전근", MajorMuscleEnum.THIGH),

        GRACILIS("GRACILIS", "박근", MajorMuscleEnum.THIGH),

//        HAMSTRING("HAMSTRING", "슬건근", BodyPartEnum.LEG),
        BICEPS_FEMORIS("BICEPS_FEMORIS", "대퇴 이두근", MajorMuscleEnum.THIGH),
        BICEPS_FEMORIS_LONGHEAD("BICEPS_FEMORIS_LONGHEAD", "대퇴 이두 장두", MajorMuscleEnum.THIGH),
        BICEPS_FEMORIS_SHORTHEAD("BICEPS_FEMORIS_SHORTHEAD", "대퇴 이두 단두", MajorMuscleEnum.THIGH),

        SEMITENDINOSUS("SEMITENDINOSUS", "반건양근", MajorMuscleEnum.THIGH),
        SEMIMEMBRANOSUS("SEMIMEMBRANOSUS", "반막양근", MajorMuscleEnum.THIGH),

//        CALF("CALF", "종아리", BodyPartEnum.LEG),
        GASTROCNEMIUS("GASTROCNEMIUS", "비복근", MajorMuscleEnum.CALF),
        SOLEUS("SOLEUS", "가자미근", MajorMuscleEnum.CALF),
        TIBIALIS_ANTERIOR("TIBIALIS_ANTERIOR", "전경근", MajorMuscleEnum.CALF);

        private final String name;
        private final String desc;
        private final MajorMuscleEnum majorMuscleEnum;

        MinorMuscleEnum(String name, String desc, MajorMuscleEnum majorMuscle) {
            this.name = name;
            this.desc = desc;
            this.majorMuscleEnum = majorMuscle;
        }

        @JsonCreator
        public static MinorMuscleEnum fromJson(@JsonProperty("name") String name) {
            for (MinorMuscleEnum minorMuscleEnum : MinorMuscleEnum.values()) {
                if (minorMuscleEnum.name.equals(name)) {
                    return minorMuscleEnum;
                }
            }
            return null;
        }

        // String to Enum
        @JsonCreator
        public static MinorMuscleEnum fromString(String str) {
            return MinorMuscleEnum.valueOf(str);
        }

        public static Optional<MinorMuscleEnum> findByDesc(String desc) {
            return Arrays.stream(MinorMuscleEnum.values())
                    .filter(muscle -> muscle.getDesc().equals(desc))
                    .findFirst();
        }
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum MajorMuscleEnum {
        SHOULDER("SHOULDER", "어깨"),
        BICEPS("BICEPS", "이두"),
        TRICEPS("TRICEPS", "삼두"),
        ANTEBRACHIAL("ANTEBRACHIAL", "전완"),
        CHEST("CHEST", "가슴"),
        BACK("BACK", "등"),
        SERRATUS_ANTERIOR("SERRATUS_ANTERIOR", "전거근"),
        ABDOMEN("ABDOMEN", "복근"),
        HIP("HIP", "엉덩이"),
        THIGH("THIGH", "허벅지"),
        CALF("LEG", "종아리");

        private final String name;
        private final String desc;

        MajorMuscleEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        @JsonCreator
        public static MajorMuscleEnum fromJson(@JsonProperty("name") String name,
                                               @JsonProperty("desc") String desc) {
            for (MajorMuscleEnum majorMuscleEnum : MajorMuscleEnum.values()) {
                if (majorMuscleEnum.desc.equals(name) && majorMuscleEnum.desc.equals(desc)) {
                    return majorMuscleEnum;
                }
            }
            return null;
        }

        // String to Enum
        @JsonCreator
        public static MajorMuscleEnum fromString(String str) {
            return MajorMuscleEnum.valueOf(str);
        }
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum DifficultyEnum {
        BEGINNER("BEGINNER", "기초"),
        ADVANCED("ADVANCED", "심화");

        private final String name;
        private final String desc;

        DifficultyEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        @JsonCreator
        public static DifficultyEnum fromJson(@JsonProperty("name") String name) {
            for (DifficultyEnum difficultyEnum : DifficultyEnum.values()) {
                if (difficultyEnum.name.equals(name)) {
                    return difficultyEnum;
                }
            }
            return null;
        }

        // String to Enum
        @JsonCreator
        public static DifficultyEnum fromString(String str) {
            return DifficultyEnum.valueOf(str);
        }
    }
}

// bodyPart 컬럼 추가
// api에도 수정
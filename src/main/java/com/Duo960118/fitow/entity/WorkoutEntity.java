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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

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
    private String workoutName;

    // 운동 난이도
    @Enumerated(EnumType.STRING)
    private DifficultyEnum workoutDifficulty;

    // 주동근
    @ElementCollection
    @CollectionTable(name = "AGONIST_MUSCLE_ENUMS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
    @Enumerated(EnumType.STRING)
    private Set<MuscleEnum> agonistMuscleEnums;

    // 길항근
    @ElementCollection
    @CollectionTable(name = "ANTAGONIST_MUSCLE_ENUMS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
        @Enumerated(EnumType.STRING)
    private Set<MuscleEnum> antagonistMuscleEnums;

    // 협응근
    @ElementCollection
    @CollectionTable(name = "SYNERGIST_MUSCLE_ENUMS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
    @Enumerated(EnumType.STRING)
    private Set<MuscleEnum> synergistMuscleEnums;

    // 운동 설명
    @ElementCollection
    @CollectionTable(name = "DESCRIPTIONS", joinColumns = @JoinColumn(name = "WORKOUT_ID"))
    private List<String> descriptions;

    // 영상 경로 or 외부 영상 링크
    private String mediaFileName;

    @Builder
    public WorkoutEntity(String workoutName, DifficultyEnum workoutDifficulty, Set<MuscleEnum> agonistMuscle, Set<MuscleEnum> antagonistMuscle, Set<MuscleEnum> synergistMuscle, List<String> descriptions, String mediaFileName) {
        this.workoutName = workoutName;
        this.workoutDifficulty = workoutDifficulty;
        this.agonistMuscleEnums = agonistMuscle;
        this.antagonistMuscleEnums = antagonistMuscle;
        this.synergistMuscleEnums = synergistMuscle;
        this.descriptions = descriptions;
        this.mediaFileName = mediaFileName;
    }

    public void updateWorkoutEntity(WorkoutDto.PostWorkoutRequestDto editWorkoutRequest) {
        this.workoutName = editWorkoutRequest.getWorkoutName();
        this.workoutDifficulty = editWorkoutRequest.getWorkoutDifficulty();
        this.agonistMuscleEnums = new HashSet<>(editWorkoutRequest.getAgonistMuscles());
        this.antagonistMuscleEnums = new HashSet<>(editWorkoutRequest.getAntagonistMuscles());
        this.synergistMuscleEnums = new HashSet<>(editWorkoutRequest.getSynergistMuscles());
        this.descriptions = editWorkoutRequest.getDescriptions();
        this.mediaFileName = editWorkoutRequest.getMediaFileName();
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum MuscleEnum {
        // 등
        TRAPEZIUS("TRAPEZIUS", "승모근", BodyPartEnum.BACK),
        UPPER_TRAPEZIUS("UPPER_TRAPEZIUS", "상부 승모근", BodyPartEnum.BACK),
        MIDDLE_TRAPEZIUS("MIDDLE_TRAPEZIUS", "중부 승모근", BodyPartEnum.BACK),
        LOWER_TRAPEZIUS("LOWER_TRAPEZIUS", "하부 승모근", BodyPartEnum.BACK),

        LATISSIMUS_DORSI("LATISSIMUS_DORSI", "광배근", BodyPartEnum.BACK),
        UPPER_LATISSIMUS_DORSI("UPPER_LATISSIMUS_DORSI", "상부 광배근", BodyPartEnum.BACK),
        LOWER_LATISSIMUS_DORSI("LOWER_LATISSIMUS_DORSI", "하부 광배근", BodyPartEnum.BACK),

        TERES_MAJOR("TERES_MAJOR", "대원근", BodyPartEnum.BACK),
        TERES_MINOR("TERES_MINOR", "소원근", BodyPartEnum.BACK),
        LEVATOR_SCAPULA("LEVATOR_SCAPULA", "견갑거근", BodyPartEnum.BACK),

        RHOMBOIDS("RHOMBOIDS", "능형근", BodyPartEnum.BACK),
        MAJOR_RHOMBOIDS("MAJOR_RHOMBOIDS", "대능형근", BodyPartEnum.BACK),
        MINOR_RHOMBOIDS("MINOR_RHOMBOIDS", "소능형근", BodyPartEnum.BACK),

        ERECTOR_MUSCLE("ERECTOR_MUSCLE", "기립근", BodyPartEnum.BACK),

        // 어깨
        DELTOID("DELTOID", "삼각근", BodyPartEnum.SHOULDER),
        ANTERIOR_DELTOID("ANTERIOR_DELTOID", "전면 삼각근", BodyPartEnum.SHOULDER),
        LATERAL_DELTOID("LATERAL_DELTOID", "측면 삼각근", BodyPartEnum.SHOULDER),
        POSTERIOR_DELTOID("POSTERIOR_DELTOID", "후면 삼각근", BodyPartEnum.SHOULDER),
        CORACOBRACHIALIS("CORACOBRACHIALIS", "오훼완근", BodyPartEnum.SHOULDER),

        SUPRASPINATUS("SUPRASPINATUS", "극상근", BodyPartEnum.SHOULDER),
        INFRASPINATUS("INFRASPINATUS", "극하근", BodyPartEnum.SHOULDER),

        SUBSCAPULARIS("SUBSCAPULARIS", "견갑하근", BodyPartEnum.SHOULDER),

        // 가슴
        PECTORALIS_MAJOR("PECTORALIS_MAJOR","대흉근", BodyPartEnum.CHEST),
        UPPER_PECTORALIS_MAJOR("UPPER_PECTORALIS_MAJOR","상부 대흉근", BodyPartEnum.CHEST),
        MIDDLE_PECTORALIS_MAJOR("MIDDLE_PECTORALIS_MAJOR","중부 대흉근", BodyPartEnum.CHEST),
        LOWER_PECTORALIS_MAJOR("LOWER_PECTORALIS_MAJOR","하부 대흉근", BodyPartEnum.CHEST),

        PECTORALIS_MINOR("PECTORALIS_MINOR","소흉근", BodyPartEnum.CHEST),

        SUBCLAVIUS("SUBCLAVIUS","쇄골하근", BodyPartEnum.CHEST),

        SERRATUS_ANTERIOR("SERRATUS_ANTERIOR","전거근", BodyPartEnum.CHEST),

        // 팔
        BICEPS("BICEPS", "이두근", BodyPartEnum.ARM),
        BICEPS_LONG_HEAD("BICEPS_LONG_HEAD","상완 이두 장두", BodyPartEnum.ARM),
        BICEPS_SHORT_HEAD("BICEPS_SHORT_HEAD","상완 이두 단두", BodyPartEnum.ARM),
        BRACHIALIS("BRACHIALIS","상완근", BodyPartEnum.ARM),

        TRICEPS("TRICEPS", "삼두근", BodyPartEnum.ARM),
        TRICEPS_LONG_HEAD("TRICEPS_LONG_HEAD","상완 삼두 장두", BodyPartEnum.ARM),
        TRICEPS_LATERAL("TRICEPS_LATERAL","상완 삼두 외측", BodyPartEnum.ARM),
        TRICEPS_MEDIAL_HEAD("TRICEPS_MEDIAL_HEAD","상완 삼두 내측", BodyPartEnum.ARM),

        ANTEBRACHIAL("ANTEBRACHIAL","전완근", BodyPartEnum.ARM),
        WRIST_EXTENSORS("WRIST_EXTENSORS","전완 신근", BodyPartEnum.ARM),
        WRIST_FLEXORS("WRIST_FLEXORS","전완 굴근", BodyPartEnum.ARM),

        // 복근
        RECTUS_ABDOMINIS("RECTUS_ABDOMINIS","복직근", BodyPartEnum.ABDOMEN),
        EXTERNAL_OBLIQUE("EXTERNAL_OBLIQUE","외복사근", BodyPartEnum.ABDOMEN),
        INTERNAL_OBLIQUE("INTERNAL_OBLIQUE","내복사근", BodyPartEnum.ABDOMEN),
        TRANSVERSE_OBLIQUE("TRANSVERSE_OBLIQUE","복횡근", BodyPartEnum.ABDOMEN),
        QUADRATUS_LUMBORUM("QUADRATUS_LUMBORUM","요방형근", BodyPartEnum.ABDOMEN),

        // 둔근
        GLUTEUS("GLUTEUS", "둔근", BodyPartEnum.HIP),
        GLUTEUS_MAXIMUS("GLUTEUS_MAXIMUS", "대둔근", BodyPartEnum.HIP),
        GLUTEUS_MEDIUS("GLUTEUS_MEDIUS", "중둔근", BodyPartEnum.HIP),
        GLUTEUS_MINIMUS("GLUTEUS_MINIMUS", "소둔근", BodyPartEnum.HIP),
        LATERAL_ROTATORS_HIP_JOINT("LATERAL_ROTATORS_HIP_JOINT", "고관절 외회전근", BodyPartEnum.HIP),
        PSOAS_MAJOR("PSOAS_MAJOR", "대요근", BodyPartEnum.HIP),
        ILIACUS("ILIACUS", "장골근", BodyPartEnum.HIP),
        PECTINEUS("PECTINEUS", "치골근", BodyPartEnum.HIP),

        // 하체
        QUADRICEPS_FEMORIS("QUADRICEPS_FEMORIS", "대퇴 사두근", BodyPartEnum.LEG),
        RECTUS_FEMORIS("RECTUS_FEMORIS", "대퇴직근", BodyPartEnum.LEG),
        VASTUS_MEDIALIS("VASTUS_MEDIALIS", "내측광근", BodyPartEnum.LEG),
        VASTUS_LATERALIS("VASTUS_LATERALIS", "외측광근", BodyPartEnum.LEG),
        TENSOR_FASCIAE_LATAE("TENSOR_FASCIAE_LATAE", "대퇴근막장근", BodyPartEnum.LEG),

        ADDUCTOR("ADDUCTOR", "내전근", BodyPartEnum.LEG),
        ADDUCTOR_MAGNUS("ADDUCTOR_MAGNUS", "대내전근", BodyPartEnum.LEG),
        ADDUCTOR_LONGUS("ADDUCTOR_LONGUS", "장내전근", BodyPartEnum.LEG),
        ADDUCTOR_BREVIS("ADDUCTOR_BREVIS", "단내전근", BodyPartEnum.LEG),
        GRACILIS("GRACILIS", "박근", BodyPartEnum.LEG),

        HAMSTRING("HAMSTRING", "슬건근", BodyPartEnum.LEG),
        BICEPS_FEMORIS("BICEPS_FEMORIS", "대퇴 이두근", BodyPartEnum.LEG),
        SEMITENDINOSUS("SEMITENDINOSUS", "반건양근", BodyPartEnum.LEG),
        SEMIMEMBRANOSUS("SEMIMEMBRANOSUS", "반막양근", BodyPartEnum.LEG),

        CALF("CALF", "종아리", BodyPartEnum.LEG),
        GASTROCNEMIUS("GASTROCNEMIUS", "비복근", BodyPartEnum.LEG),
        SOLEUS("SOLEUS", "가자미근", BodyPartEnum.LEG),
        TIBIALIS_ANTERIOR("TIBIALIS_ANTERIOR", "전경근", BodyPartEnum.LEG);



        private final String name;
        private final String desc;
        private final BodyPartEnum bodyPart;

        MuscleEnum(String name, String desc, BodyPartEnum bodyPart) {
            this.name = name;
            this.desc = desc;
            this.bodyPart = bodyPart;
        }

        @JsonCreator
        public static MuscleEnum fromJson(@JsonProperty("name") String name) {
            for (MuscleEnum muscleEnum : MuscleEnum.values()) {
                if (muscleEnum.name.equals(name)) {
                    return muscleEnum;
                }
            }
            return null;
        }

        // String to Enum
        @JsonCreator
        public static MuscleEnum fromString(String str) {
            return MuscleEnum.valueOf(str);
        }

        public static Optional<MuscleEnum> findByDesc(String desc) {
            return Arrays.stream(MuscleEnum.values())
                    .filter(muscle -> muscle.getDesc().equals(desc))
                    .findFirst();
        }
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum BodyPartEnum {
        ARM("ARM", "팔"),
        BACK("BACK", "등"),
        ABDOMEN("ABDOMEN", "복부"),
        LEG("LEG", "다리"),
        CHEST("CHEST", "가슴"),
        SHOULDER("SHOULDER", "어깨"),
        HIP("HIP", "엉덩이");

        private final String name;
        private final String desc;

        BodyPartEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        @JsonCreator
        public static BodyPartEnum fromJson(@JsonProperty("name") String name,
                                            @JsonProperty("desc") String desc) {
            for (BodyPartEnum bodyPartEnum : BodyPartEnum.values()) {
                if (bodyPartEnum.desc.equals(name) && bodyPartEnum.desc.equals(desc)) {
                    return bodyPartEnum;
                }
            }
            return null;
        }

        // String to Enum
        @JsonCreator
        public static BodyPartEnum fromString(String str) {
            return BodyPartEnum.valueOf(str);
        }
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum DifficultyEnum {
        BEGINNER("BEGINNER", "초급"),
        INTERMEDIATE("INTERMEDIATE", "중급"),
        ADVANCED("ADVANCED", "고급");

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
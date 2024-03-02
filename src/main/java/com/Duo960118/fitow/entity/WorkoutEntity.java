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
public class WorkoutEntity extends TimeStampEntity{
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
    private DifficultyEnum workoutDifficulty;

    // JPA에서는 @Convert 어노테이션을 Entity의 프로퍼티에 적용하면,
    // @Converter 어노테이션이 적용된 클래스와 자동으로 매핑하여,
    // DB에 저장된 varChar 데이터타입을 Enum 으로 매핑시켜,
    // Java에서 Enum 클래스로 핸들링 할 수 있도록 해줍니다.

    // 주동근
    @ElementCollection
    @CollectionTable(name = "AGONIST_MUSCLE_ENUMS", joinColumns =
    @JoinColumn(name = "WORKOUT_ID"))
    private Set<MuscleEnum> agonistMuscleEnums;

    // 길항근
    @ElementCollection
    @CollectionTable(name = "ANTAGONIST_MUSCLE_ENUMS", joinColumns =
    @JoinColumn(name = "WORKOUT_ID"))
    private Set<MuscleEnum> antagonistMuscleEnums;

    // 협응근
    @ElementCollection
    @CollectionTable(name = "SYNERGIST_MUSCLE_ENUMS", joinColumns =
    @JoinColumn(name = "WORKOUT_ID"))
    private Set<MuscleEnum> synergistMuscleEnums;

    // 운동 설명
    @ElementCollection
    @CollectionTable(name = "DESCRIPTIONS", joinColumns =
    @JoinColumn(name = "WORKOUT_ID"))
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

    public void updateWorkoutEntity(WorkoutDto.PostWorkoutRequestDto editWorkoutRequest){
        this.workoutName = editWorkoutRequest.getWorkoutName();
        this.workoutDifficulty = editWorkoutRequest.getWorkoutDifficulty();
        this.agonistMuscleEnums =  new HashSet<>(editWorkoutRequest.getAgonistMuscles());
        this.antagonistMuscleEnums = new HashSet<>(editWorkoutRequest.getAntagonistMuscles());
        this.synergistMuscleEnums = new HashSet<>(editWorkoutRequest.getSynergistMuscles());
        this.descriptions = editWorkoutRequest.getDescriptions();
        this.mediaFileName = editWorkoutRequest.getMediaFileName();
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum MuscleEnum{
        // 광배근
        LATISSIMUS_DORSI_MUSCLE("광배근",BodyPartEnum.BACK),
        
        // 승모근
        TRAPEZIUS("승모근",BodyPartEnum.BACK),
        
        // 기립근
        ERECTOR_MUSCLE("기립근",BodyPartEnum.BACK),
        
        // 대퇴 사두근
        QUADRICEPS_FEMORIS_MUSCLE("대퇴 사두근",BodyPartEnum.LEG),
        
        // 햄스트링 ( 대퇴 이두근 + 반건양근, 반막양근 )
        HAMSTRING("햄스트링",BodyPartEnum.LEG),
        
        // 대둔근
        GLUTEUS_MAXIMUS_MUSCLE("대둔근",BodyPartEnum.LEG),
        
        // 이두근
        BICEPS_MUSCLE("이두근",BodyPartEnum.ARM),
        
        // 삼두근
        TRICEPS_MUSCLE("삼두근",BodyPartEnum.ARM);

        // description
        private final String desc;
        private final BodyPartEnum bodyPart;

        MuscleEnum(String desc, BodyPartEnum bodyPart) {
            this.desc = desc;
            this.bodyPart = bodyPart;
        }

        // mapper 사용 안할려고
        // @JsonCreator
        // Json to Enum
        @JsonCreator
        public static MuscleEnum fromJson(@JsonProperty("desc")String desc ,
                                           @JsonProperty("bodyPart") BodyPartEnum bodyPart){
            for(MuscleEnum muscleEnum : MuscleEnum.values()){
                if(muscleEnum.desc.equals(desc) && muscleEnum.bodyPart.equals(bodyPart)){
                    return muscleEnum;
                }
            }
            return null;
        }

        // String to Enum
        @JsonCreator
        public static MuscleEnum fromString(String str){
            return MuscleEnum.valueOf(str);
        }

        // desc에 맞는 MuscleEnum 찾기
        public static Optional<MuscleEnum> findByDesc(String desc){
            return Arrays.stream(MuscleEnum.values()).
                    filter(muscle->muscle.getDesc().equals(desc)).findFirst();
        }
    }

    @Getter
    public enum BodyPartEnum{
        ARM("팔"),
        BACK("등"),
        ABDOMEN("복부"),
        LEG("다리"),
        CHEST("가슴"),
        SHOULDER("어깨"),
        HEAP("엉덩이");

        private final String desc;

        BodyPartEnum(String desc){
            this.desc = desc;
        }
    }

    @Getter
    public enum DifficultyEnum{
        BEGINNER("초급"),
        INTERMEDIATE("중급"),
        ADVANCED("고급");

        private final String desc;

        DifficultyEnum(String desc){
            this.desc = desc;
        }
    }
}

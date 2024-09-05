package com.Duo960118.fitow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "CALCINFO")
@EntityListeners(AuditingEntityListener.class)
public class CalculatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long calcId;
    @PositiveOrZero(message = "{PositiveOrZero.age}")
    @NotNull(message = "{NotNull.age}")
    @Max(value = 150, message = "{Max.age}")
    private int age;
    @NotNull(message = "{NotNull.gender}")
    private GenderEnum gender;
    @Positive(message = "{Positive.height}")
    @NotNull(message = "{NotNull.height}")
    @Max(value = 250, message = "{height}")
    private float height;
    @Positive(message = "{Positive.weight}")
    @NotNull(message = "{NotNull.weight}")
    @Max(value = 999, message = "{Max.weight}")
    private float weight;
    @Positive(message = "{Positive.bmr}")
    @Max(value = 4000, message = "{Max.bmr}")
    private int bmr;
    @NotNull(message = "{NotNull.activityLevel}")
    private ActivityLevelEnum activityLevel;
    @NotNull(message = "{NotNull.dietGoal}")
    private DietGoalEnum dietGoal;
    private double carb;
    private double protein;
    private double fat;
    @ManyToOne // 한 명의 유저가 여러개 계산 결과 보유 가능
    @JoinColumn(name = "userId")
    private UserEntity userEntity;
    // DB 규모 유추 방지를 위해 보여줄 때 uuid
    @Embedded
    private UuidEntity uuidEntity;
    private LocalDate calcDate;
    private CalcCategoryEnum calcCategory;

    // 계산정보 엔티티 생성자
    @Builder
    public CalculatorEntity(UserEntity userEntity, int age, GenderEnum gender, float height, float weight, int bmr,
                            ActivityLevelEnum activityLevel, DietGoalEnum dietGoal, double carb, double protein, double fat, CalcCategoryEnum calcCategory, LocalDate calcDate) {
        this.userEntity = userEntity;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.bmr = bmr;
        this.activityLevel = activityLevel;
        this.dietGoal = dietGoal;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.calcCategory = calcCategory;
        this.calcDate = calcDate;
    }

    public void updateUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // 활동량
    @Getter
    public enum ActivityLevelEnum {
        VERY_LOW,
        LOW,
        NORMAL,
        HIGH,
        VERY_HIGH;

        // String to Enum
        @JsonCreator
        public static ActivityLevelEnum fromString(String str) {
            return ActivityLevelEnum.valueOf(str);
        }
    }

    // 목표
    @Getter
    public enum DietGoalEnum {
        HIGH_LOSS,
        LOSS,
        KEEP,
        GAIN,
        HIGH_GAIN;

        // String to Enum
        @JsonCreator
        public static DietGoalEnum fromString(String str) {
            return DietGoalEnum.valueOf(str);
        }
    }

    // 세부 계산
    @Getter
    public enum CalcCategoryEnum {
        NORMAL,
        LOADING,
        BANTING;

        // String to Enum
        @JsonCreator
        public static CalcCategoryEnum fromString(String str) {
            return CalcCategoryEnum.valueOf(str);
        }
    }
}

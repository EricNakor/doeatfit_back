package com.Duo960118.fitow.entity;

import jakarta.persistence.*;
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
public class CalculateInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long calcId;
    private int age;
    private GenderEnum gender;
    private float height;
    private float weight;
    private int bmr;
    private activityLevelEnum activityLevel;
    private dietGoalEnum dietGoal;
    private double carb;
    private double protein;
    private double fat;
    @ManyToOne // 한 명의 유저가 여러개 계산 결과 보유 가능
    @JoinColumn(name = "userId")
    private UserEntity userEntity;
    // DB 규모 유추 방지를 위해 보여줄 때 uuid
    @Embedded
    private UuidEntity uuidEntity;
    @CreatedDate
    private LocalDate calcDate;
    private calcCategoryEnum calcCategory;

    // 계산정보 엔티티 생성자
    @Builder
    public CalculateInfoEntity(UserEntity userEntity, int age, GenderEnum gender, float height, float weight, int bmr,
                               activityLevelEnum activityLevel, dietGoalEnum dietGoal, double carb, double protein, double fat, calcCategoryEnum calcCategory) {
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
    }

    public void updateUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // 활동량
    @Getter
    public enum activityLevelEnum {
        VERY_LOW,
        LOW,
        NORMAL,
        HIGH,
        VERY_HIGH
    }

    // 목표
    @Getter
    public enum dietGoalEnum {
        HIGH_LOSS,
        LOSS,
        KEEP,
        GAIN,
        HIGH_GAIN
    }

    // 세부 계산
    @Getter
    public enum calcCategoryEnum {
        NORMAL,
        LOADING,
        BANTING
    }

}

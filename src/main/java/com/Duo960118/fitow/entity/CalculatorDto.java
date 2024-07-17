package com.Duo960118.fitow.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

public class CalculatorDto {

    // 계산정보 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CalcRequestDto {
        private int age;
        private GenderEnum gender;
        private float height;
        private float weight;
        private int bmr;
        private CalculateInfoEntity.activityLevelEnum activityLevel;
        private CalculateInfoEntity.dietGoalEnum dietGoal;
        private CalculateInfoEntity.calcCategoryEnum calcCategory;
    }

    // 계산 결과
    public record CalcResponseDto(UUID uuid, double carb, double protein, double fat){}
//    @Getter
//    @RequiredArgsConstructor
//    public static class CalcResponseDto {
//        private final UUID uuid;
//        private final double carb;
//        private final double protein;
//        private final double fat;
//    }
    // record 는 불변 객체로 abstract 로 선언할 수 없으며 암시적으로 final 로 선언된다.
    // 한 번 값이 정해지면 setter 를 통해 값을 변경할 수 없으며 상속을 할 수 없다.
    // 필드 별 getter가 자동으로 생성된다.
    // 모든 멤버 변수를 인자로 하는 public 생성자를 자동으로 생성한다.
    // java 16부터 정식지원

    // 계산 결과 리스트
    @Getter
    @RequiredArgsConstructor
    public static class CalcResultDto {
        private final UUID uuid;
        private final int age;
        private final GenderEnum gender;
        private final float height;
        private final float weight;
        private final int bmr;
        private final CalculateInfoEntity.activityLevelEnum activityLevel;
        private final CalculateInfoEntity.dietGoalEnum dietGoal;
        private final double carb;
        private final double protein;
        private final double fat;
        private final LocalDate calcDate;
        private final CalculateInfoEntity.calcCategoryEnum calcCategory;
    }

    // 로딩, 벤딩 계산값 입력 리스트
    @Getter
    @RequiredArgsConstructor
    public static class AdvancedCalcListDto {
        private final UUID uuid;
        private final double carb;
        private final double protein;
        private final double fat;
        private final int bmr;
        private final LocalDate calcDate;
    }

    // 고급 계산 DTO
    @Getter
    @Setter
    @AllArgsConstructor
    public static class AdvancedCalcRequestDto {
        private final UUID uuid;
        private CalculateInfoEntity.calcCategoryEnum calcCategory;
    }
}

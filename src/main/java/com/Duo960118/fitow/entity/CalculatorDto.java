package com.Duo960118.fitow.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    }

    // 계산 결과
    @Getter
    @RequiredArgsConstructor
    public static class CalcResponseDto {
        private final UUID uuid;
        private final double carb;
        private final double protein;
        private final double fat;
    }

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
    }
}

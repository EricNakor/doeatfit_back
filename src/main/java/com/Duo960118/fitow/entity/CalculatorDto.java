package com.Duo960118.fitow.entity;

import com.Duo960118.fitow.annotaion.Enum;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public class CalculatorDto {

    // 계산정보 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CalcRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
        @PositiveOrZero(message = "{PositiveOrZero.age}")
        @NotNull(message = "{NotNull.age}")
        @Max(value = 150, message = "{Max.age}")
        private int age;
        @Enum(enumClass = GenderEnum.class, message = "{Enum.gender}")
        @NotBlank(message = "{NotBlank.gender}")
        private String gender;
        @Positive(message = "{Positive.height}")
        @NotNull(message = "{NotNull.height}")
        @Max(value = 250, message = "{Max.height}")
        private float height;
        @Positive(message = "{Positive.weight}")
        @NotNull(message = "{NotNull.weight}")
        @Max(value = 999, message = "{Max.weight}")
        private float weight;
        @Positive(message = "{Positive.bmr}")
        @Max(value = 4000, message = "{Max.bmr}")
        private int bmr;
        @Enum(enumClass = CalculatorEntity.ActivityLevelEnum.class, message = "{Enum.activityLevel}")
        @NotBlank(message = "{NotBlank.activityLevel}")
        private String activityLevel;
        // private CalculateInfoEntity.ActivityLevelEnum activityLevel;
        // enum validation annotation 을 사용하기 위해
        // DTO 내에서 String 으로 받아와야 Valid 에서 비교가 가능
        @Enum(enumClass = CalculatorEntity.DietGoalEnum.class, message = "{Enum.dietGoal}")
        @NotBlank(message = "{NotBlank.dietGoal}")
        private String dietGoal;
        @Enum(enumClass = CalculatorEntity.CalcCategoryEnum.class, message = "{Enum.calcCategory}")
        @NotBlank(message = "{NotBlank.calcCategory}")
        private String calcCategory;
    }

    // 계산 결과
    public record CalcResponseDto(UUID uuid, double carb, double protein, double fat) {
    }
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
        private final String activityLevel;
        private final String dietGoal;
        private final double carb;
        private final double protein;
        private final double fat;
        private final LocalDate calcDate;
        private final String calcCategory;
    }

    // 로딩, 벤딩 계산값 입력 리스트
//    @Getter
//    @RequiredArgsConstructor
//    public static class AdvancedCalcListDto {
//        private final UUID uuid;
//        private final double carb;
//        private final double protein;
//        private final double fat;
//        private final int bmr;
//        private final LocalDate calcDate;
//    }

    // 고급 계산 요청
    @Getter
    @Setter
    @AllArgsConstructor
    public static class AdvancedCalcRequestDto {
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private String email;
        @NotNull(message = "{NotNull.uuid}")
        private final UUID uuid;
        @Enum(enumClass = CalculatorEntity.CalcCategoryEnum.class, message = "{Enum.calcCategory}")
        @NotBlank(message = "{NotBlank.calcCategory}")
        private String calcCategory;
    }

    // 계산 결과 페이징 DTO
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResultListPageDto {
        private final String email;
        private final Pageable pageable;
    }
}

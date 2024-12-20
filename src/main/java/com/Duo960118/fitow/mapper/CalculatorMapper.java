package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.CalculatorDto;
import com.Duo960118.fitow.entity.CalculatorEntity;
import org.springframework.context.annotation.Bean;

public class CalculatorMapper {

    // 계산 결과 Entity 를 Dto 형식으로 변환
    @Bean
    public static CalculatorDto.CalcResultDto entityToCalcResultDto(CalculatorEntity calculatorEntity) {
        return new CalculatorDto.CalcResultDto(
                calculatorEntity.getUuidEntity().getUuid(), calculatorEntity.getAge(),
                calculatorEntity.getGender(), calculatorEntity.getHeight(), calculatorEntity.getWeight(),
                calculatorEntity.getBmr(), calculatorEntity.getActivityLevel().toString(), calculatorEntity.getDietGoal().toString(),
                calculatorEntity.getCarb(), calculatorEntity.getProtein(), calculatorEntity.getFat(),
                calculatorEntity.getCalcDate(), calculatorEntity.getCalcCategory().toString()
        );
    }

//    public static CalculatorDto.AdvancedCalcListDto entityToAdvancedCalcListDto(CalculateInfoEntity calculateEntity) {
//        return new CalculatorDto.AdvancedCalcListDto(
//                calculateEntity.getUuidEntity().getUuid(), calculateEntity.getCarb(),
//                calculateEntity.getProtein(), calculateEntity.getFat(),
//                calculateEntity.getBmr(), calculateEntity.getCalcDate()
//        );
//    }
}

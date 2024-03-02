package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.CalculatorDto;
import com.Duo960118.fitow.entity.CalculateInfoEntity;
import org.springframework.context.annotation.Bean;

public class CalculatorMapper {

    // 계산 결과 Entity 를 Dto 형식으로 변환
    @Bean
    public static CalculatorDto.CalcResultDto entityToCalcResultDto(CalculateInfoEntity calculatorEntity) {
        return new CalculatorDto.CalcResultDto(
                calculatorEntity.getUuidEntity().getUuid(), calculatorEntity.getAge(),
                calculatorEntity.getGender(), calculatorEntity.getHeight(), calculatorEntity.getWeight(),
                calculatorEntity.getBmr(), calculatorEntity.getActivityLevel(), calculatorEntity.getDietGoal(),
                calculatorEntity.getCarb(), calculatorEntity.getProtein(), calculatorEntity.getFat(),
                calculatorEntity.getCalcDate()
        );
    }
}

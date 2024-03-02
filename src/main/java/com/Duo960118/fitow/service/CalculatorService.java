package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.CalculatorDto;
import com.Duo960118.fitow.entity.GenderEnum;
import com.Duo960118.fitow.entity.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CalculatorService {

    //todo: front에서 계산하기
    int calculateAge(LocalDate birth);

    // BMR 이 없을 경우 BMR 추정치 계산
    int calculateGenderBmr(int age, GenderEnum gender, float weight, float height);

    // 유지 칼로리 계산
    double calcStandardDiet(CalculatorDto.CalcRequestDto calcRequest);

    // 다이어트 목표별 계산
    double calcGoalDiet(CalculatorDto.CalcRequestDto calcRequest);

    // 유지 칼로리, 다이어트 목표를 이용한 일일 권장 섭취량 계산
    @Transactional
    CalculatorDto.CalcResponseDto calculate(CalculatorDto.CalcRequestDto calcRequest, UserEntity userEntity);

    // 계산 결과 한 개
    CalculatorDto.CalcResultDto getCalcResult(UUID uuid);

    // 계산 결과 히스토리
    List<CalculatorDto.CalcResultDto> getCalcResults(UserEntity userEntity);

    // 계산 결과 삭제
    @Transactional
    void deleteResult(UUID uuid);

    // 계산 결과 페이징 처리
    List<CalculatorDto.CalcResultDto> getCalcResultsPage(UserEntity userEntity, PageRequest pageRequest);
}
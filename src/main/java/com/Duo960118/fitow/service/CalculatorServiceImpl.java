package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.mapper.CalculatorMapper;
import com.Duo960118.fitow.repository.CalculatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CalculatorServiceImpl implements CalculatorService {
    private final CalculatorRepository calculatorRepository;

    //todo: front에서 계산하기
    @Override
    public int calculateAge(LocalDate birth) {
        return Period.between(birth, LocalDate.now()).getYears();
    }

    // BMR 이 없을 경우 BMR 추정치 계산
    public int calculateGenderBmr(int age, GenderEnum gender, float weight, float height) {
        if (gender == GenderEnum.FEMALE) {
            return (int) (655 + 9.6 * weight + 1.7 * height - 4.7 * age);
        } else {
            return (int) (66 + 13.7 * weight + 5 * height - 6.8 * age);
        }
        //todo: 헤리스-베네딕트 공식 > 1984개정
        //      미플린 세인트 지어 공식 > 1990 이후 최고의 예측 공식
        //      캐치 맥아들 공식
        //      커닝햄 공식
    }

    // 유지 칼로리
    public double calcStandardDiet(CalculatorDto.CalcRequestDto calcRequest) {
        double result = 0.0;
        switch (calcRequest.getActivityLevel()) {
            case VERY_LOW -> {
                result = calcRequest.getBmr() * 1.2;
            }
            case LOW -> {
                result = calcRequest.getBmr() * 1.375;
            }
            case NORMAL -> {
                result = calcRequest.getBmr() * 1.55;
            }
            case HIGH -> {
                result = calcRequest.getBmr() * 1.725;
            }
            case VERY_HIGH -> {
                result = calcRequest.getBmr() * 1.9;
            }
        }
        return result;
    }

    // 다이어트 목표별 계산
    public double calcGoalDiet(CalculatorDto.CalcRequestDto calcRequest) {
        double calcStandDiet = calcStandardDiet(calcRequest);
        double result = 0.0;
        switch (calcRequest.getDietGoal()) {
            case HIGH_LOSS -> {
                result = calcStandDiet * 0.9;
            }
            case LOSS -> {
                result = calcStandDiet * 0.8;
            }
            case KEEP -> {
                result = calcStandDiet;
            }
            case GAIN -> {
                result = calcStandDiet * 1.1;
            }
            case HIGH_GAIN -> {
                result = calcStandDiet * 1.2;
            }
        }
        return result;
    }

    @Transactional
    // 유지 칼로리, 다이어트 목표를 이용한 일일 권장 섭취량 계산
    public CalculatorDto.CalcResponseDto calculate(CalculatorDto.CalcRequestDto calcRequest, UserEntity userEntity) {
        // bmr 값이 없을 때
        // todo: front에서 bmr값 안적으면 0 으로 넣어서 보내기
        if (calcRequest.getBmr() == 0) {
            int bmr = calculateGenderBmr(calcRequest.getAge(), calcRequest.getGender(), calcRequest.getWeight(), calcRequest.getHeight());
            calcRequest.setBmr(bmr);
        }
        double goalDiet = calcGoalDiet(calcRequest);
        // 일일 영양소 섭취
        double carb = (goalDiet * 0.5) / 4;
        double protein = (goalDiet * 0.3) / 4;
        double fat = (goalDiet * 0.2) / 9;

        // 저장할 데이터 빌드
        CalculateInfoEntity calculatorEntity = CalculateInfoEntity.builder()
                .userEntity(userEntity)
                .age(calcRequest.getAge())
                .gender(calcRequest.getGender())
                .height(calcRequest.getHeight())
                .weight(calcRequest.getWeight())
                .bmr(calcRequest.getBmr())
                .activityLevel(calcRequest.getActivityLevel())
                .dietGoal(calcRequest.getDietGoal())
                .carb(carb)
                .protein(protein)
                .fat(fat)
                .build();
        // 리포지토리 저장
        calculatorRepository.save(calculatorEntity);
        // 반환
        return new CalculatorDto.CalcResponseDto(calculatorEntity.getUuidEntity().getUuid(), carb, protein, fat);
    }

    @Override
    // 계산 결과 한 개
    public CalculatorDto.CalcResultDto getCalcResult(UUID uuid) {
        CalculateInfoEntity calculatorEntity = calculatorRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new RuntimeException("계산 결과가 없습니다"));

        return CalculatorMapper.entityToCalcResultDto(calculatorEntity);
    }

    // 계산 결과 히스토리
    @Override
    public List<CalculatorDto.CalcResultDto> getCalcResults(UserEntity userEntity) {
        List<CalculateInfoEntity> calculateInfoEntity = calculatorRepository.findByUserEntityUserIdOrderByCalcIdDesc(userEntity.getUserId());

        // 매퍼
        return calculateInfoEntity.stream().map(CalculatorMapper::entityToCalcResultDto).collect(Collectors.toList());
    }

    // 계산 결과 삭제
    @Override
    @Transactional
    public void deleteResult(UUID uuid) {
        calculatorRepository.deleteByUuidEntityUuid(uuid);
    }

    // 계산 결과 히스토리 페이징 처리
    // todo: 페이징처리 서비스와 히스토리 서비스 중 하나 택 1
    @Override
    public List<CalculatorDto.CalcResultDto> getCalcResultsPage(UserEntity userEntity, PageRequest pageRequest) {
        return calculatorRepository.findAllByUserEntityUserId(userEntity.getUserId(), pageRequest)
                .stream().map(CalculatorMapper::entityToCalcResultDto).collect(Collectors.toList());

    }
}
package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.mapper.CalculatorMapper;
import com.Duo960118.fitow.repository.CalculatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
                result = calcStandDiet * 0.8;
            }
            case LOSS -> {
                result = calcStandDiet * 0.9;
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

    @Override
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
        CalculateInfoEntity normalCalcEntity = CalculateInfoEntity.builder()
                .userEntity(userEntity)
                .calcCategory(CalculateInfoEntity.calcCategoryEnum.NORMAL)
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
        calculatorRepository.save(normalCalcEntity);
        // 반환
        return new CalculatorDto.CalcResponseDto(normalCalcEntity.getUuidEntity().getUuid(), carb, protein, fat);
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

    @Override
    // 로딩, 벤딩할 값 리스트
    public List<CalculatorDto.AdvancedCalcListDto> getAdvancedCalcList(UserEntity userEntity, PageRequest pageRequest) {
        Page<CalculateInfoEntity> calculateInfoEntity = calculatorRepository.findByUserEntityUserIdAndCalcCategoryOrderByCalcIdDesc(userEntity.getUserId(), CalculateInfoEntity.calcCategoryEnum.NORMAL, pageRequest);

        return calculateInfoEntity.stream().map(CalculatorMapper::entityToAdvancedCalcListDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    // 로딩, 벤딩 계산하기
    public CalculatorDto.CalcResponseDto calculateAdvanced(CalculatorDto.AdvancedCalcRequestDto calcRequest, UserEntity userEntity) {

        CalculateInfoEntity calculateEntity = calculatorRepository.findByUuidEntityUuid(calcRequest.getUuid()).orElseThrow(() -> new RuntimeException("존재하지 않는 결과"));

        double advancedCarb, advancedProtein, advancedFat;
        switch (calcRequest.getCalcCategory()) {
            case LOADING:
                advancedCarb = calculateEntity.getCarb() * 2;
                advancedProtein = 0;
                advancedFat = 0;
                break;
            case BANTING:
                advancedCarb = calculateEntity.getCarb() / 2;
                advancedProtein = calculateEntity.getProtein();
                advancedFat = calculateEntity.getFat();
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 카테고리: " + calcRequest.getCalcCategory());
        }
        // 저장할 데이터 빌드
        CalculateInfoEntity advancedCalcEntity = CalculateInfoEntity.builder()
                .userEntity(userEntity)
                .calcCategory(calcRequest.getCalcCategory())
                .age(calculateEntity.getAge())
                .gender(calculateEntity.getGender())
                .height(calculateEntity.getHeight())
                .weight(calculateEntity.getWeight())
                .bmr(calculateEntity.getBmr())
                .activityLevel(calculateEntity.getActivityLevel())
                .dietGoal(calculateEntity.getDietGoal())
                .carb(advancedCarb)
                .protein(advancedProtein)
                .fat(advancedFat)
                .build();
        // 리포지토리 저장
        calculatorRepository.save(advancedCalcEntity);
        // 반환
        return new CalculatorDto.CalcResponseDto(advancedCalcEntity.getUuidEntity().getUuid(), advancedCarb, advancedProtein, advancedFat);
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
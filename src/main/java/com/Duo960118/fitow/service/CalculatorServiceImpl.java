package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.mapper.CalculatorMapper;
import com.Duo960118.fitow.repository.CalculatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CalculatorServiceImpl implements CalculatorService {
    private final CalculatorRepository calculatorRepository;
    private final UserService userService;

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
        switch (CalculatorEntity.ActivityLevelEnum.fromString(calcRequest.getActivityLevel())) {
            case VERY_LOW -> result = calcRequest.getBmr() * 1.2;
            case LOW -> result = calcRequest.getBmr() * 1.375;
            case NORMAL -> result = calcRequest.getBmr() * 1.55;
            case HIGH -> result = calcRequest.getBmr() * 1.725;
            case VERY_HIGH -> result = calcRequest.getBmr() * 1.9;
        }
        return result;
    }

    // 다이어트 목표별 계산
    public double calcGoalDiet(CalculatorDto.CalcRequestDto calcRequest) {
        double calcStandDiet = calcStandardDiet(calcRequest);
        double result = 0.0;
        switch (CalculatorEntity.DietGoalEnum.fromString(calcRequest.getDietGoal())) {
            case HIGH_LOSS -> result = calcStandDiet * 0.8;
            case LOSS -> result = calcStandDiet * 0.9;
            case KEEP -> result = calcStandDiet;
            case GAIN -> result = calcStandDiet * 1.1;
            case HIGH_GAIN -> result = calcStandDiet * 1.2;
        }
        return result;
    }

    @Override
    @Transactional
    // 유지 칼로리, 다이어트 목표를 이용한 일일 권장 섭취량 계산
    public CalculatorDto.CalcResponseDto calculate(CalculatorDto.CalcRequestDto calcRequest) {
        UserEntity userEntity = userService.findByEmail(calcRequest.getEmail());
        // bmr 값이 없을 때
        // todo: front에서 bmr값 안적으면 0 으로 넣어서 보내기
        if (calcRequest.getBmr() == 0) {
            int bmr = calculateGenderBmr(calcRequest.getAge(), GenderEnum.fromString(calcRequest.getGender()), calcRequest.getWeight(), calcRequest.getHeight());
            calcRequest.setBmr(bmr);
        }
        double goalDiet = calcGoalDiet(calcRequest);
        // 일일 영양소 섭취
        double carb = (goalDiet * 0.5) / 4;
        double protein = (goalDiet * 0.3) / 4;
        double fat = (goalDiet * 0.2) / 9;

        // 저장할 데이터 빌드
        CalculatorEntity normalCalcEntity = CalculatorEntity.builder()
                .userEntity(userEntity)
                .calcCategory(CalculatorEntity.CalcCategoryEnum.NORMAL)
                .age(calcRequest.getAge())
                .gender(GenderEnum.fromString(calcRequest.getGender()))
                .height(calcRequest.getHeight())
                .weight(calcRequest.getWeight())
                .bmr(calcRequest.getBmr())
                .activityLevel(CalculatorEntity.ActivityLevelEnum.fromString(calcRequest.getActivityLevel()))
                .dietGoal(CalculatorEntity.DietGoalEnum.fromString(calcRequest.getDietGoal()))
                .carb(carb)
                .protein(protein)
                .fat(fat)
                .build();
        // 리포지토리 저장
        calculatorRepository.save(normalCalcEntity);
        // 반환
        return new CalculatorDto.CalcResponseDto(normalCalcEntity.getUuidEntity().getUuid(), carb, protein, fat);
    }

    // 로딩, 벤딩 계산하기
    @Override
    @Transactional
    public CalculatorDto.CalcResponseDto calculateAdvanced(CalculatorDto.AdvancedCalcRequestDto calcRequest ) {
        UserEntity userEntity = userService.findByEmail(calcRequest.getEmail());
        // 예외: 존재하지 않는 계산 결과
        CalculatorEntity calculateEntity = calculatorRepository.findByUuidEntityUuid(calcRequest.getUuid()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계산 결과" + calcRequest.getUuid()));

        double advancedCarb, advancedProtein, advancedFat;
        switch (CalculatorEntity.CalcCategoryEnum.fromString(calcRequest.getCalcCategory())) {
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
                // 예외: 고급 계산기 알 수 없는 카테고리
                throw new IllegalArgumentException("알 수 없는 카테고리: " + calcRequest.getCalcCategory());
        }

        // 저장할 데이터 빌드
        CalculatorEntity advancedCalcEntity = CalculatorEntity.builder()
                .userEntity(userEntity)
                .calcCategory(CalculatorEntity.CalcCategoryEnum.fromString(calcRequest.getCalcCategory()))
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

    // 계산 결과 한 개
    @Override
    public CalculatorDto.CalcResultDto getCalcResult(UUID uuid) {
        // 예외: 존재하지 않는 계산결과
        CalculatorEntity calculatorEntity = calculatorRepository.findByUuidEntityUuid(uuid).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계산결과 " + uuid));

        return CalculatorMapper.entityToCalcResultDto(calculatorEntity);
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
    public List<CalculatorDto.CalcResultDto> getCalcResultsPage(CalculatorDto.ResultListPageDto resultListPageDto) {
        UserEntity userEntity = userService.findByEmail(resultListPageDto.getEmail());

        return calculatorRepository.findAllByUserEntityUserId(userEntity.getUserId(), resultListPageDto.getPageable())
                .stream()
                .map(CalculatorMapper::entityToCalcResultDto)
                .collect(Collectors.toList());
    }

    // 로딩, 밴딩할 값 리스트
    @Override
    public List<CalculatorDto.CalcResultDto> getAdvancedCalcPage(CalculatorDto.ResultListPageDto resultListPageDto) {
        UserEntity userEntity = userService.findByEmail(resultListPageDto.getEmail());
        Page<CalculatorEntity> calculateInfoEntity = calculatorRepository.findByUserEntityUserIdAndCalcCategoryOrderByCalcIdDesc(userEntity.getUserId(), CalculatorEntity.CalcCategoryEnum.NORMAL, resultListPageDto.getPageable());
        return calculateInfoEntity
                .stream()
                .map(CalculatorMapper::entityToCalcResultDto)
                .collect(Collectors.toList());
    }

    // 회원 탈퇴 시 외부키 null로 변경
    @Override
    @Transactional
    public void updateForeignKeysNull(Long userId){
        List<CalculatorEntity> calculateInfoEntities = calculatorRepository.findAllByUserEntityUserId(userId);

        // 외래 키를 null로 설정
        for (CalculatorEntity calculatorEntity : calculateInfoEntities) {
            calculatorEntity.updateUserEntity(null);
        }
    }
}
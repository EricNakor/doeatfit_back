package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.response.ApiResponse;
import com.Duo960118.fitow.service.CalculatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/calculate")
public class CalculatorApiController {
    private final CalculatorService calculatorService;
    // ResponseEntity = ResponseHeader + ResponseBody
    // REST API 로 만든다면 클라이언트와 서버 간의 통신에 필요한 정보를 제공해야 합니다.
    // It represents an HTTP response, allowing you to customize the response status, headers, and body that your API returns to the client
    // ex. HTTP 200 ok + data{ ... }

    // 계산하기_일반 (일일 섭취량 계산)
    @PostMapping("normal")
    public ApiResponse<CalculatorDto.CalcResponseDto> calculateDailyIntake(@Valid @RequestBody CalculatorDto.CalcRequestDto calcRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        calcRequest.setEmail(userDetails.getUsername());
        return ApiResponse.success(calculatorService.calculate(calcRequest));
    }

    // 계산하기_고급 (벤딩, 로딩 계산)
    @PostMapping("advanced")
    public ApiResponse<CalculatorDto.CalcResponseDto> calculateAdvance(@Valid @RequestBody CalculatorDto.AdvancedCalcRequestDto calcRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        calcRequest.setEmail(userDetails.getUsername());
        return ApiResponse.success(calculatorService.calculateAdvanced(calcRequest));
    }

    // 계산 기록 전체 불러오기
    @GetMapping("history")
    public ApiResponse<List<CalculatorDto.CalcResultDto>> getResultHistory(
            @PageableDefault(page = 0, size = 10, sort = "calcId", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CalculatorDto.ResultListPageDto resultListPageDto = new CalculatorDto.ResultListPageDto(customUserDetails.getUsername(), pageable);

        return ApiResponse.success(calculatorService.getCalcResultsPage(resultListPageDto));
    }

    // 로딩, 밴팅 할 값 불러오기
    @GetMapping("history/category_normal")
    public ApiResponse<List<CalculatorDto.CalcResultDto>> getNormalResultHistory(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                    @PageableDefault(page = 0, size = 10, sort = "calcId")Pageable pageable) {
        CalculatorDto.ResultListPageDto resultListPageDto = new CalculatorDto.ResultListPageDto(customUserDetails.getUsername(),pageable);
        List<CalculatorDto.CalcResultDto> result = calculatorService.getAdvancedCalcPage(resultListPageDto);

        return ApiResponse.success(result);
    }

    // 계산 결과 자세히 보기
    @GetMapping("result/{uuid}")
    public ApiResponse<CalculatorDto.CalcResultDto> getResultDetail(@PathVariable("uuid") UUID uuid, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.success(calculatorService.getCalcResult(uuid));
    }

    // 계산 기록 삭제
    @DeleteMapping("result/{uuid}")
    public ApiResponse<Object> deleteResult(@PathVariable("uuid") UUID uuid) {
        calculatorService.deleteResult(uuid);
        return ApiResponse.success(null);
    }

//    // 활동량 Enum 반환
//    @GetMapping("activity-level-enum")
//    public ResponseEntity<CalculateInfoEntity.activityLevelEnum[]> getActivityLevelEnums() {
//        return ResponseEntity.ok().body(CalculateInfoEntity.activityLevelEnum.values());
//    }
//
//    // 다이어트 목표 Enum 반환
//    @GetMapping("diet-goal-enum")
//    public ResponseEntity<CalculateInfoEntity.dietGoalEnum[]> getDietGoalEnums() {
//        return ResponseEntity.ok().body(CalculateInfoEntity.dietGoalEnum.values());
//    }
//
//    // 고급 계산 카테고리 Enum 반환
//    @GetMapping("calculator-category")
//    public ResponseEntity<CalculateInfoEntity.calcCategoryEnum[]> getCalcCategoryEnums() {
//        return ResponseEntity.ok().body(CalculateInfoEntity.calcCategoryEnum.values());
//    }
}

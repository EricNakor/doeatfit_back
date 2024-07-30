package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.service.CalculatorService;
import com.Duo960118.fitow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/calculate")
public class CalculatorApiController {
    private final CalculatorService calculatorService;
    private final UserService userService;
    // ResponseEntity = ResponseHeader + ResponseBody
    // REST API 로 만든다면 클라이언트와 서버 간의 통신에 필요한 정보를 제공해야 합니다.
    // It represents an HTTP response, allowing you to customize the response status, headers, and body that your API returns to the client
    // ex. HTTP 200 ok + data{ ... }

    // 일일 섭취량 계산
    @PostMapping("normal")
    public ResponseEntity<CalculatorDto.CalcResponseDto> calculateDailyIntake(@RequestBody CalculatorDto.CalcRequestDto calcRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity userEntity = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok().body(calculatorService.calculate(calcRequest, userEntity));
    }

    // 벤딩, 로딩 계산
    @PostMapping("advanced")
    public ResponseEntity<CalculatorDto.CalcResponseDto> calculateAdvance(@RequestBody CalculatorDto.AdvancedCalcRequestDto calcRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity userEntity = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok().body(calculatorService.calculateAdvanced(calcRequest, userEntity));
    }

    // 계산 기록 전체 불러오기
    @GetMapping("history")
    public ResponseEntity<List<CalculatorDto.CalcResultDto>> getResultHistory(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Sort sort = Sort.by("calcId").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize).withSort(sort    );
        UserEntity userEntity = userService.findByEmail(customUserDetails.getUsername());

        return ResponseEntity.ok().body(calculatorService.getCalcResultsPage(userEntity, pageRequest));
    }

    // 로딩, 밴팅 할 값 불러오기
    @GetMapping("history/category_normal")
    public ResponseEntity<List<CalculatorDto.CalcResultDto>> getNormalResultHistory(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                    @PageableDefault(page = 0, size = 10, sort = "calcId")Pageable pageable) {
        UserEntity userEntity = userService.findByEmail(customUserDetails.getUsername());
        List<CalculatorDto.CalcResultDto> result = calculatorService.getAdvancedCalcPage(userEntity, pageable);

        return ResponseEntity.ok().body(result);
    }

    // 계산 결과 자세히 보기
    @GetMapping("result/{uuid}")
    public ResponseEntity<CalculatorDto.CalcResultDto> getResultDetail(@PathVariable("uuid") UUID uuid, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(calculatorService.getCalcResult(uuid));
    }

    // 계산 기록 삭제
    @DeleteMapping("result/{uuid}")
    public ResponseEntity<StatusResponseDto> deleteResult(@PathVariable("uuid") UUID uuid) {
        calculatorService.deleteResult(uuid);
        return ResponseEntity.ok().body(new StatusResponseDto(true));
        // todo: StatusResponseDto 활용해 리턴 수정 필요
    }

    // 활동량 Enum 반환
    @GetMapping("activity-level-enum")
    public ResponseEntity<CalculateInfoEntity.activityLevelEnum[]> getActivityLevelEnums() {
        return ResponseEntity.ok().body(CalculateInfoEntity.activityLevelEnum.values());
    }

    // 다이어트 목표 Enum 반환
    @GetMapping("diet-goal-enum")
    public ResponseEntity<CalculateInfoEntity.dietGoalEnum[]> getDietGoalEnums() {
        return ResponseEntity.ok().body(CalculateInfoEntity.dietGoalEnum.values());
    }

    // 고급 계산 카테고리 Enum 반환
    @GetMapping("calculator-category")
    public ResponseEntity<CalculateInfoEntity.calcCategoryEnum[]> getCalcCategoryEnums() {
        return ResponseEntity.ok().body(CalculateInfoEntity.calcCategoryEnum.values());
    }
}

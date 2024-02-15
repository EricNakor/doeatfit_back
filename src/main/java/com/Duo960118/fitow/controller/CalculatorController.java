package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.CalculateInfoEntity;
import com.Duo960118.fitow.entity.CalculatorDto;
import com.Duo960118.fitow.entity.CustomUserDetails;
import com.Duo960118.fitow.entity.UserEntity;
import com.Duo960118.fitow.service.CalculatorService;
import com.Duo960118.fitow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class CalculatorController {
    private final CalculatorService calculatorService;
    private final UserService userService;

    // 계산기 페이지
    @GetMapping("calculator")
    public String calculator(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        model.addAttribute("activityLevelEnums", CalculateInfoEntity.activityLevelEnum.values());
        model.addAttribute("dietGoalEnums", CalculateInfoEntity.dietGoalEnum.values());
        if (customUserDetails == null) {
            return "unExpectedError";
        }
        return "calculator/calculator";
    }

    // 계산 기록 전체 불러오기
    @GetMapping("calculator/history")
    public String getResultList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        // 결과 리스트
        UserEntity userEntity = userService.findByEmail(customUserDetails.getUsername());
        // 정렬
        Sort sort = Sort.by("calcId").descending();
        PageRequest pageRequest = PageRequest.of(0, 5).withSort(sort);
        // 리스트
        List<CalculatorDto.CalcResultDto> resultsList = calculatorService.getCalcResultsPage(userEntity,pageRequest);
        // 모델
        model.addAttribute("results", resultsList);
        return "calculator/history";
    }

    // 계산 결과 자세히 보기
    @GetMapping("calculator/result/{uuid}")
    public String getResult(@PathVariable("uuid") UUID uuid, Model model) {
        model.addAttribute("result", calculatorService.getCalcResult(uuid));
        return "calculator/result";
    }

}

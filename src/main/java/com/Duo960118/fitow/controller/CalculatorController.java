package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.*;
import com.Duo960118.fitow.service.CalculatorService;
import com.Duo960118.fitow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("calculator")
public class CalculatorController {
    private final CalculatorService calculatorService;
    private final UserService userService;

    // 게산기 인덱스
    @GetMapping("")
    public String calculator(HttpServletRequest req) {
        req.setAttribute("contentPage", "calculator/normalCalculator.html");
        return "calculator/calculatorIndex";
    }

    // 기본 계산기 페이지
    @GetMapping("normal")
    public String normalCalculator(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        model.addAttribute("activityLevelEnums", CalculateInfoEntity.activityLevelEnum.values());
        model.addAttribute("dietGoalEnums", CalculateInfoEntity.dietGoalEnum.values());
        model.addAttribute("genderEnums", GenderEnum.values());

        // 만 나이 설정
        model.addAttribute("age", calculatorService.calculateAge(customUserDetails.getUserInfo().getBirth()));

        // 성별 설정
        model.addAttribute("gender", customUserDetails.getUserInfo().getGender());

        if (customUserDetails == null) {
            return "unExpectedError";
        }
        return "calculator/normalCalculator";
    }

    // 고급 계산할 리스트값 가져오기
    @GetMapping("advanced")
    public String advancedCalculator(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
//        model.addAttribute("categoryEnums", CalculateInfoEntity.calcCategoryEnum.values());
        model.addAttribute("categoryEnums", new CalculateInfoEntity.calcCategoryEnum[]
                {CalculateInfoEntity.calcCategoryEnum.LOADING, CalculateInfoEntity.calcCategoryEnum.BANTING});
        // 리스트
        UserEntity userEntity = userService.findByEmail(customUserDetails.getUsername());
        // 정렬
        Sort sort = Sort.by("calcId").descending();
        PageRequest pageRequest = PageRequest.of(0, 5).withSort(sort);
        // 리스트
        List<CalculatorDto.AdvancedCalcListDto> resultsList = calculatorService.getAdvancedCalcList(userEntity, pageRequest);
        // 모델
        model.addAttribute("lists", resultsList);

        return "calculator/advancedCalculator";
    }

    // 계산 기록 전체 불러오기
    @GetMapping("history")
    public String getResultList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        // 결과 리스트
        UserEntity userEntity = userService.findByEmail(customUserDetails.getUsername());
        // 정렬
        Sort sort = Sort.by("calcId").descending();
        PageRequest pageRequest = PageRequest.of(0, 10).withSort(sort);
        // 리스트
        List<CalculatorDto.CalcResultDto> resultsList = calculatorService.getCalcResultsPage(userEntity, pageRequest);
        // 모델
        model.addAttribute("results", resultsList);
        return "calculator/history";
    }

    // 계산 결과 자세히 보기
    @GetMapping("result/{uuid}")
    public String getResult(@PathVariable("uuid") UUID uuid, Model model) {
        model.addAttribute("result", calculatorService.getCalcResult(uuid));
        return "calculator/result";
    }
}
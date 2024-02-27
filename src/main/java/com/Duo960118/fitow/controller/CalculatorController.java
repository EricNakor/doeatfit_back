package com.Duo960118.fitow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalculatorController {

    // 계산기 페이지
    @GetMapping("calculator")
    public String calculator()
    {
        return "calculator/calculator";
    }

    // 결과
    @GetMapping("calculator/result")
    public String result()
    {
        return "calculator/result";
    }
}

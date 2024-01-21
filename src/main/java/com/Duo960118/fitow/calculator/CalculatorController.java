package com.Duo960118.fitow.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalculatorController {

    // 계산기 페이지
    @GetMapping("calculator")
    public String calculator()
    {
        return "calculator";
    }

    // 결과
    @GetMapping("result")
    public String result()
    {
        return "result";
    }
}

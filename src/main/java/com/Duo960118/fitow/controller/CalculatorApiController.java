package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.entity.CalculatorDto;
import com.Duo960118.fitow.entity.CustomUserDetails;
import com.Duo960118.fitow.entity.StatusResponseDto;
import com.Duo960118.fitow.entity.UserEntity;
import com.Duo960118.fitow.service.CalculatorService;
import com.Duo960118.fitow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CalculatorApiController {
    private final CalculatorService calculatorService;
    private final UserService userService;
    // ResponseEntity = ResponseHeader + ResponseBody
    // REST API로 만든다면 클라이언트와 서버 간의 통신에 필요한 정보를 제공해야 합니다.
    // It represents an HTTP response, allowing you to customize the response status, headers, and body that your API returns to the client
    // ex) HTTP 200 ok + data{ ... }

    // 일일 섭취량 계산
    @PostMapping("/calculate")
    public ResponseEntity<CalculatorDto.CalcResponseDto> calculateDailyIntake(@RequestBody CalculatorDto.CalcRequestDto calcRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity userEntity = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok().body(calculatorService.calculate(calcRequest, userEntity));
    }

    // 계산 기록 전체 불러오기
    @GetMapping("/calculator/history")
    public ResponseEntity<List<CalculatorDto.CalcResultDto>> getResultHistory(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        UserEntity userEntity = userService.findByEmail(customUserDetails.getUsername());

        return ResponseEntity.ok().body(calculatorService.getCalcResultsPage(userEntity,pageRequest));
    }

    // 계산 결과 자세히 보기
    @GetMapping("calculator/result/{uuid}")
    public ResponseEntity<CalculatorDto.CalcResultDto> getResultDetail(@PathVariable("uuid") UUID uuid, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(calculatorService.getCalcResult(uuid));
    }

    // 계산 기록 삭제
    @DeleteMapping("calculator/result/{uuid}")
    public ResponseEntity<StatusResponseDto> deleteResult(@PathVariable("uuid") UUID uuid) {
        calculatorService.deleteResult(uuid);
        return ResponseEntity.ok().body( new StatusResponseDto(true));
        // todo: StatusResponseDto 활용해 리턴 수정 필요
    }
}

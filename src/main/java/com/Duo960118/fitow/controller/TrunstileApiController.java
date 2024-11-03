package com.Duo960118.fitow.controller;

import com.Duo960118.fitow.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/api")
@RestController
@Slf4j
public class TrunstileApiController {
    @Value("${turnstile.secret-key}")
    private String secretKey;

    @PostMapping("/verify-turnstile")
    public ApiResponse<String> verifyTurnstile(@RequestParam String token) {

        // 턴스타일 API를 호출하여 토큰 검증
        String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", secretKey);
        requestMap.add("response", token);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestMap, String.class);

        log.info(response.getBody());
        // 검증 결과 반환
        return ApiResponse.success(response.getBody());
    }
}
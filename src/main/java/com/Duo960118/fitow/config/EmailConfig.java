package com.Duo960118.fitow.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class EmailConfig {
    // 발신자 정보
    @Value("${spring.mail.username}")
    private String serviceEmail;
}
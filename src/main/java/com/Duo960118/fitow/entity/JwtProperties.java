package com.Duo960118.fitow.entity;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

//@Component Annotation: Added to allow Spring to manage this class as a bean.
@Component
public class JwtProperties {
    //Non-static secretString: Changed SECRET_STRING to a non-static field secretString to allow Spring to inject the value.
    private static String secretString;

    public static final long ACCESS_TOKEN_TIME = Duration.ofMinutes(30).toMillis(); // 30분
    public static final long REFRESH_TOKEN_TIME = Duration.ofDays(7).toMillis(); // 7일 in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_KEY = "AccessToken";
    public static final String EXCEPTION_STRING = "Exception";
    public static SecretKey secretKey;

    //Setter Method for secretString: Used @Value on a setter method to inject the value into the static variable.
    @Value("${spring.jwt.secret}")
    public void setSecretString(String secret) {
        secretString = secret;
    }

    //@PostConstruct Method: Used @PostConstruct to initialize secretKey after the bean is created.
    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(secretString.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
}

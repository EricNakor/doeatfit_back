package com.Duo960118.fitow.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Builder
@Getter
@RedisHash(value = "tokens")
public class TokensEntity {
    @Id
    private String email;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    @TimeToLive
    private final Long ttl;

    public void updateAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
}

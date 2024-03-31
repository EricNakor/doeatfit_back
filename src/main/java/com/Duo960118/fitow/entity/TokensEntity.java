package com.Duo960118.fitow.entity;


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

    private String accessToken;

    private String refreshToken;

    @TimeToLive
    private final Long ttl;

    public void updateAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
}

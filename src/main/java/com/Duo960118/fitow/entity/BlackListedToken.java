package com.Duo960118.fitow.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Builder
@Getter
@RedisHash(value = "blacklisted_token")
public class BlackListedToken {
    @Id
    private String accessToken;

    @TimeToLive
    private Long ttl;
}

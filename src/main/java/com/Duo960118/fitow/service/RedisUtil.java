package com.Duo960118.fitow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    // {"96jwjw..." : "123454"}
    // key : email(String), value : auth token(String) 이므로 따로 RedisTemplate 안만들어도 됨
    // 이미 구현돼있는 StringRedisTemplate 사용

    private final StringRedisTemplate redisTemplate;


    // key값에 해당하는 value 가져오기
    public Optional<String> getData(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    // key, value, 유효기간 저장
    public void setDataExp(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    // key,value 제거
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public void renameKey(String oldKey, String newKey){
        redisTemplate.rename(oldKey,newKey);
    }
}


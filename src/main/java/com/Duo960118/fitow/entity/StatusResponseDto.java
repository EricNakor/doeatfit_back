package com.Duo960118.fitow.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// 상태응답 DTO
@Getter
@Setter
@RequiredArgsConstructor
public class StatusResponseDto {
    private final boolean success;
}
package com.Duo960118.fitow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE,
    FEMALE;

    @JsonCreator
    public static GenderEnum fromString(String str) {
        return GenderEnum.valueOf(str);
    }
}
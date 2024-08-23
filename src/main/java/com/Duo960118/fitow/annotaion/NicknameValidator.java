package com.Duo960118.fitow.annotaion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class NicknameValidator implements ConstraintValidator<Nickname, String> {
    private static final int MIN_SIZE = 2;
    private static final int MAX_SIZE = 12;

    private static final String regexNickname =
            "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{"
                    + MIN_SIZE + "," + MAX_SIZE + "}$";


    @Override
    public void initialize(Nickname constraintAnnotation) {
    }

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        boolean isValidNickname = nickname.matches(regexNickname);
        if (!isValidNickname) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            MessageFormat.format("닉네임은 {0}자리 이상 {1}자리 이하로 영어 대소문자, 한글, 숫자만 허용됩니다.", MIN_SIZE, MAX_SIZE))
                    .addConstraintViolation();
        }
        return isValidNickname;
    }

    public boolean isValid(String password) {
        return password.matches(regexNickname);
    }
}
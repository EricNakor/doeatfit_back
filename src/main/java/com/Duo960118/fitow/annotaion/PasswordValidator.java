package com.Duo960118.fitow.annotaion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final int MIN_SIZE = 8;
    private static final int MAX_SIZE = 20;

    private static final String regexPassword =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{"
                    + MIN_SIZE + "," + MAX_SIZE + "}$";
    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean isValidPassword = password.matches(regexPassword);
        if (!isValidPassword) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            MessageFormat.format("비밀번호는 {0}자리 이상 {1}자리 이하로 특수문자(@, $, !, %, *, ?, &, #), 숫자, 대소문자 각 1개 이상 포함되어야 합니다.", MIN_SIZE, MAX_SIZE))
                    .addConstraintViolation();
        }
        return isValidPassword;
    }

    public boolean isValid(String password) {
        return password.matches(regexPassword);
    }
}
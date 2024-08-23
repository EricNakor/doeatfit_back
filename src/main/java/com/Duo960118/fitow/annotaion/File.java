package com.Duo960118.fitow.annotaion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface File {
    String message() default "File not supported.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedFileExt();
    long fileSizeLimit();
}
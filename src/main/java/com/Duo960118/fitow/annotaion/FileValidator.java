package com.Duo960118.fitow.annotaion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.Objects;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {
    private static final Logger log = LoggerFactory.getLogger(FileValidator.class);
    private File annotation;

    private static final long MB = 1048576;

    @Override
    public void initialize(File constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        StringBuilder allowedStrings = new StringBuilder();
        for(String fileExtension : annotation.allowedFileExt()) {
            allowedStrings.append(fileExtension);
            allowedStrings.append(", ");
        }
        allowedStrings.delete(allowedStrings.length() - 2, allowedStrings.length());

        // 빈 파일이면 false
        if (file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("빈 파일입니다.")
                    .addConstraintViolation();
            return false;
        }

        // 파일 확장자 추출
        String fileName = file.getOriginalFilename();
        String fileExt = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 파일 크기 확인
        if( file.getSize() > annotation.fileSizeLimit()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("최대 파일 크기를 초과하였습니다.{0} mb 이하로 업로드 해주세요.",annotation.fileSizeLimit()/MB))
                    .addConstraintViolation();
            return false;
        }


        // 이미지 파일 확장자 리스트에 있는 모든 확장자와 어노테이션에서 읽은 string의 파일의 확장자를 비교
        for(String fileExtension : annotation.allowedFileExt()) {
            if(fileExtension.equals(fileExt)){
                return true;
            }
        }

        String message = MessageFormat.format("지원하지 않은 확장자 입니다. [{0}]", allowedStrings.toString());

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}
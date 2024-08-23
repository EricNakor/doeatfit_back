package com.Duo960118.fitow.entity;

import com.Duo960118.fitow.annotaion.Enum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

// service에서 controller로 넘겨주는 dto는 @RequiredArgsConstructor
// web client에서 controller 넘겨 주는 dto는 @NoArgsConstructor
public class NoticeDto {

    // 공지 작성 요청
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostNoticeRequestDto {
        private String nickName;
        @Size(min=5, max=100, message = "{Size.title}")
        @NotBlank(message = "{NotBlank.title}")
        private String title;
        @NotBlank(message = "{NotBlank.content}")
        private String content;
        @Enum(enumClass = NoticeEntity.NoticeCategoryEnum.class, message = "{Enum.noticeCategory}")
        @NotBlank(message = "{NotBlank.noticeCategory}")
        private String category;
    }

    // 공지 수정 요청
    @Getter
    @Setter
    @NoArgsConstructor
    public static class EditNoticeRequestDto {
        private UUID uuid;
        @NotBlank(message = "{NotBlank.title}")
        @Size(min=5, max=100, message = "{Size.title}")
        private String title;
        @NotBlank(message = "{NotBlank.content}")
        private String content;
        @Enum(enumClass = NoticeEntity.NoticeCategoryEnum.class, message = "{Enum.noticeCategory}")
        @NotBlank(message = "{NotBlank.noticeCategory}")
        private String category;
    }

    // 공지 작성 ResponseDto
    @Getter
    @RequiredArgsConstructor
    public static class PostNoticeResponseDto{
        private final UUID uuid;
    }
    
    // notice detail을 NoticeController에 전달할 때 사용
    // NoticeService -> NoticeController
    @Getter
    @RequiredArgsConstructor
    public static class NoticeDetailDto {
        private final UUID uuid;
        private final String title;
        private final String content;
        private final NoticeEntity.NoticeCategoryEnum category;
        private final String author;
        private final LocalDateTime createdAt;
        private final LocalDateTime editedAt;
    }

    // notice list 표시에 사용
    @Getter
    @RequiredArgsConstructor
    public static class NoticeInfoDto {
        private final UUID uuid;
        private final String title;
        private final NoticeEntity.NoticeCategoryEnum category;
        private final String author;
        private final LocalDateTime createdAt;
    }

    // notice 검색 요청
    @Getter
    @Setter
    @Builder
    public static class SearchNoticeRequestDto {
        @Enum(enumClass = NoticeEntity.NoticeCategoryEnum.class, message = "{Enum.noticeCategory}")
        private String category;
        @Size(min=2, max=100, message = "{Size.searchString}")
        private String searchString;
    }
}

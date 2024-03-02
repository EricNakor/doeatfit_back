package com.Duo960118.fitow.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

// service에서 controller로 넘겨주는 dto는 @RequiredArgsConstructor
// web client에서 controller 넘겨 주는 dto는 @NoArgsConstructor
public class NoticeDto {
    // 공지 작성 및 수정에 사용
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostNoticeRequestDto {
        private String nickName;
        private String title;
        private String content;
        private NoticeEntity.NoticeCategoryEnum category;
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
}

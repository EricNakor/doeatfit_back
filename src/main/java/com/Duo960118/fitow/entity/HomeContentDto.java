package com.Duo960118.fitow.entity;

import com.Duo960118.fitow.annotaion.Enum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

public class HomeContentDto {
    @Getter
    public static class HomeContentInfoDto {
        private final UUID uuid;
        private final HomeContentEntity.HomeContentCategoryEnum category;
        private final Boolean isBeingUsed;
        private final String content;
        private final LocalDateTime createdAt;
        private final LocalDateTime editedAt;

        @Builder
        public HomeContentInfoDto(UUID uuid, HomeContentEntity.HomeContentCategoryEnum category, Boolean isBeingUsed, String content, LocalDateTime createdAt, LocalDateTime editedAt) {
            this.uuid = uuid;
            this.category = category;
            this.isBeingUsed = isBeingUsed;
            this.content = content;
            this.createdAt = createdAt;
            this.editedAt = editedAt;
        }
    }

    // 홈 컨텐츠 작성 요청
    @Getter
    public static class PostHomeContentRequestDto {
        @Enum(enumClass =HomeContentEntity.HomeContentCategoryEnum.class, message = "{Enum.homeCategory}")
        @NotBlank(message = "{NotBlank.homeCategory}")
        private String category;
        @NotNull(message = "{NotNull.isBeingUsed}")
        private Boolean isBeingUsed;
        @NotBlank(message = "{NotBlank.content}")
        private String content;
    }

    // 홈 컨텐츠 수정 요청
    @Getter
    @Setter
    public static class EditHomeContentRequestDto {
        private UUID uuid;
        @Enum(enumClass =HomeContentEntity.HomeContentCategoryEnum.class, message = "{Enum.homeCategory}")
        @NotBlank(message = "{Enum.homeCategory}")
        private String category;
        @NotNull(message = "{NotNull.isBeingUsed}")
        private Boolean isBeingUsed;
        @NotBlank(message = "{NotBlank.content}")
        private String content;
    }
}

package com.Duo960118.fitow.entity;

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

    @Getter
    public static class PostHomeContentRequestDto {
        private HomeContentEntity.HomeContentCategoryEnum category;
        private Boolean isBeingUsed;
        private String content;
    }

    @Getter
    @Setter
    public static class EditHomeContentRequestDto {
        private UUID uuid;
        private HomeContentEntity.HomeContentCategoryEnum category;
        private Boolean isBeingUsed;
        private String content;
    }
}

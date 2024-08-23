package com.Duo960118.fitow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "HOME_CONTENT")
public class HomeContentEntity extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long homeContentId;
    @NotNull(message = "{NotNull.isBeingUsed}")
    private Boolean isBeingUsed;
    @NotNull(message = "{NotNull.homeCategory}")
    private HomeContentCategoryEnum category;
    @Column(unique = true)
    @NotBlank(message = "{NotBlank.content}")
    private String content;
    @Embedded
    private UuidEntity uuidEntity;

    @Builder
    public HomeContentEntity(Long homeContentId, Boolean isBeingUsed, HomeContentCategoryEnum category, String content, UuidEntity uuidEntity) {
        this.homeContentId = homeContentId;
        this.isBeingUsed = isBeingUsed;
        this.category = category;
        this.content = content;
        this.uuidEntity = uuidEntity;
    }

    public void updateHomeContent(HomeContentDto.EditHomeContentRequestDto editHomeContentRequest) {
        this.category = HomeContentCategoryEnum.fromString(editHomeContentRequest.getCategory());
        this.content = editHomeContentRequest.getContent();
        this.isBeingUsed = editHomeContentRequest.getIsBeingUsed();
    }

    public void updateIsBeingUsed(Boolean isBeingUsed) {
        this.isBeingUsed = isBeingUsed;
    }

    public enum HomeContentCategoryEnum {
        HOME_INTRODUCE_1,
        HOME_INTRODUCE_2,
        HOME_INTRODUCE_3;

        @JsonCreator
        public static HomeContentCategoryEnum fromString(String str) {
            return HomeContentCategoryEnum.valueOf(str);
        }
    }
}

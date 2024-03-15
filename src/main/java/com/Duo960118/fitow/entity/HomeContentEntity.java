package com.Duo960118.fitow.entity;

import jakarta.persistence.*;
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
    private Boolean isBeingUsed;
    private HomeContentCategoryEnum category;

    @Column(unique = true)
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
        this.content=editHomeContentRequest.getContent();
        this.isBeingUsed =editHomeContentRequest.getIsBeingUsed();
    }

    public void updateIsBeingUsed(Boolean isBeingUsed){
        this.isBeingUsed =isBeingUsed;
    }

    public enum HomeContentCategoryEnum {
        HOME_INTRODUCE_1,
        HOME_INTRODUCE_2,
        HOME_INTRODUCE_3
    }
}

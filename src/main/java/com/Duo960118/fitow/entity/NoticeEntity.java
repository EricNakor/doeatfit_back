package com.Duo960118.fitow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "NOTICE")
public class NoticeEntity extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long noticeId;

    // FK
    // DB는 오브젝트를 저장할 수 없지만 자바는 오브젝트를 저장할 수 있다.
    // 참조 할 테이블
    // join할 테이블의 pk 이외에 다른 column도 join 하려면 referencedColumnName 사용해서 구분 해야함
    @ManyToOne
    // Many = Board, User = One 한명의 유저는 여러개의 게시글을 쓸 수 있다.
    @JoinColumn(name = "userId", referencedColumnName = "userId")// foreign key (userId) references User (id)
    private UserEntity userEntity;

    // 생략가능한 어노테이션
    // 외부에 notice 보여줄 때 uuid를 노출, PK는 내부용으로만
    @Embedded
    private UuidEntity uuidEntity;

    @NotBlank(message = "{NotBlank.title}")
    @Size(min=5, max=100, message = "{Size.title}")
    private String title;

    // mediumtext : ~ 16mb
    // longtext : ~ 4gb
    @Column(columnDefinition = "mediumtext")
    @NotBlank(message = "{NotBlank.content}")
    private String content;

    @NotNull(message = "{NotBlank.noticeCategory}")
    private NoticeCategoryEnum noticeCategory;

    @Builder
    public NoticeEntity(UserEntity userEntity, String title, String content, NoticeCategoryEnum noticeCategory) {
        this.userEntity = userEntity;
        this.title = title;
        this.content = content;
        this.noticeCategory = noticeCategory;
    }

    // 공지 종류
    @Getter
    public enum NoticeCategoryEnum {
        EMERGENCY("EMERGENCY"),
        NOTICE("NOTICE");

        NoticeCategoryEnum(String value) {
            this.value = value;
        }

        private final String value;

        @JsonCreator
        public static NoticeCategoryEnum fromValue(String str) {
            return NoticeCategoryEnum.valueOf(str);
        }
    }

    public void updateNotice(NoticeDto.EditNoticeRequestDto editNoticeRequest) {
        this.title = editNoticeRequest.getTitle();
        this.content = editNoticeRequest.getContent();
        this.noticeCategory = NoticeCategoryEnum.fromValue(editNoticeRequest.getCategory());

        // jpa auditing 기능에 의해 알아서 timeStampEntity의 editAt이 수정됨
    }

    public void updateUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}

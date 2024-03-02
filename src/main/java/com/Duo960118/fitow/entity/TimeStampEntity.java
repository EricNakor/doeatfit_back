package com.Duo960118.fitow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
// https://webcoding-start.tistory.com/53
// User Entity 에 TimeStamp를 상속하지 않는 이유
// 비밀번호 수정했을때만 LastModifiedDate가 찍혀야하나, 프로필사진 및 닉네임 변경시에도 수정이되며
// @PreUpdate 또는 @PrePersist를 사용하여 비밀번호만 수정했을 경우에
// 작동하는 코드를 늘리는 것 보다 따로 지정해 JPA 동작이 더 효율적이라 판단
public class TimeStampEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime editedAt;
}

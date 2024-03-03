package com.Duo960118.fitow.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

// todo: UserEntity TimeStamp(가입일자,(비밀번호)수정일자(비번교체권고)) extends 해야함 notice 할 때 하기
@Getter
// Setter는 개발자가 의도하지 않은 곳에서 유저 값이 변경되지 않도록 하기위해 사용하지 않음
// Builder를 이용해보자

// 빌더 패턴은 객체 생성 시 받지 않아야 할 데이터들도 빌더에 노출되어 수정될 수 있다.
// 따라서, 객체 생성 시 자동으로 값이 기입되는(ex. uuid, createAt, updateAt...) 필드만 모아 놓은 클래스를 만들어
// extends 하면 위에서 언급한 빌더 패턴의 단점을 보완할 수 있다...?
@NoArgsConstructor
// JPA는 DB 값을 객체 필드에 주입할 때 기본 생성자로 객체를 생성한 후 이러한 Reflection을 사용하여 값을 매핑하기 때문이다.
// Java Reflection API란?
// 구체적인 클래스 타입을 알지 못해도 그 클래스의 메소드, 타입, 변수들에 접근할 수 있도록 해주는 Java API
// Java Reflection을 활용하면 컴파일 시점이 아닌 런타임 시점에 동적으로 클래스를 객체화 하여 분석 및 추출 해낼 수 있는 프로그래밍 기법
@Entity
@Table(name = "USER")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long userId;

    @Column(unique = true)
    private String email;
    private String passwd;
    private String name;

    @Column(unique = true)
    private String nickName;
    private GenderEnum gender;
    private LocalDate birth;
    private String profileImg;
    private UserRoleEnum role;

    @CreatedDate
    private LocalDate joinDate;
    private LocalDate passwdEditDate;

    // 최초 가입 시 가입 일자 = 비밀번호 수정 일자
    @PrePersist
    public void setPwEditDate() {
        this.passwdEditDate = this.joinDate;
    }

    // 회원가입 엔티티 생성자
    @Builder
    public UserEntity(String email, String passwd, String name, String nickName, GenderEnum gender, LocalDate birth, String profileImg, UserRoleEnum role, LocalDate passwdEditDate) {
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
        this.birth = birth;
        this.profileImg = profileImg;
        this.role = role;
        this.passwdEditDate = passwdEditDate;
    }
    // 본래 빌더 어노테이션을 상단에 배치해 회원가입을 구현하였으나, 로그인 구현 중 JPA 특성으로 하단 배치하여 공존하게 만듬

    // 비밀번호 변경
    public void updatePasswd(String encodedPasswd) {
        this.passwd = encodedPasswd;
        this.passwdEditDate = LocalDate.now();
    }

    // 닉네임 변경
    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    // 프로필 이미지 변경
    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    // 회원 롤 변경
    public void updateUserRole(UserRoleEnum role){
        this.role = role;
    }

    // 회원 롤
    @Getter
    public enum UserRoleEnum {
        NORMAL("ROLE_NORMAL)"),
        VIP("ROLE_VIP"),
        ADMIN("ROLE_ADMIN");

        UserRoleEnum(String value) {
            this.value = value;
        }

        private final String value;
    }
}

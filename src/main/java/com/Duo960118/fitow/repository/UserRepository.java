package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// 회원가입
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    // null 반환 시 오류 발생 가능성이 높아 '결과 없음'을 명확히 드러내기 위해
    // 메소드 반환 타입으로 사용되도록 매우 제한적인 경우로 설계 되었다

    // 이메일 중복 확인
    boolean existsByEmail(String Email);

    // 닉네임 중복 확인
    boolean existsByNickName(String nickName);

    // 가입정보 찾기
    boolean existsByEmailAndNameAndGenderAndBirth(String email, String name, boolean gender, LocalDate birth);

    // 이메일 찾기
    @Query(value = "SELECT email FROM user WHERE name = :name AND gender = :gender AND birth = :birth", nativeQuery = true)
    List<String> findByNameAndGenderAndBirth(@Param("name") String name, @Param("gender") boolean gender, @Param("birth") LocalDate birth);

    Optional<UserEntity> findByNickName(String nickName);
}

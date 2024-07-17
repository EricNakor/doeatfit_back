package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// interface로 선언하는 이유?
public interface NoticeRepository extends JpaRepository<NoticeEntity,Long> {

    // Pageable을 이용한 findAll
    @Override
    Page<NoticeEntity> findAll(Pageable pageable);

    // Sort를 이용한 findAll
    @Override
    List<NoticeEntity> findAll(Sort sort);

    // 일치하는 uuid를 가진 notice entity를 찾아 삭제
    void deleteByUuidEntityUuid(UUID uuid);

    Optional<NoticeEntity> findByUuidEntityUuid(UUID uuid);
    
    // 매개변수로 받은 카테고리에 해당하고, 매개변수로 받은 String을 포함하는 제목을 찾기
    List<NoticeEntity> findByNoticeCategoryAndTitleContaining(@Param("category") NoticeEntity.NoticeCategoryEnum category,@Param("searchString") String searchString);

    boolean existsByUuidEntityUuid(UUID uuid);

    // 조회 회원의 공지 찾기
    List<NoticeEntity> findByUserEntityUserId(Long useId);

}

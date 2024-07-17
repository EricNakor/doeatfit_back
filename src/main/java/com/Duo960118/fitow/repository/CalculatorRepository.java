package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.CalculateInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalculatorRepository extends JpaRepository<CalculateInfoEntity, Long> {
    // uuid 찾기
    Optional<CalculateInfoEntity> findByUuidEntityUuid(UUID uuid);

    // 기록 불러오기
    List<CalculateInfoEntity> findByUserEntityUserIdOrderByCalcIdDesc(Long userId);

    // 카테고리 값이 0인 것만 불러오기
    Page<CalculateInfoEntity> findByUserEntityUserIdAndCalcCategoryOrderByCalcIdDesc(Long userId, CalculateInfoEntity.calcCategoryEnum calcCategoryEnum, Pageable pageable);

    // 기록 삭제
    void deleteByUuidEntityUuid(UUID uuid);

    // 페이징 처리
    Page<CalculateInfoEntity> findAllByUserEntityUserId(Long userId, Pageable pageable);

    List<CalculateInfoEntity> findAllByUserEntityUserId(Long userId);
}
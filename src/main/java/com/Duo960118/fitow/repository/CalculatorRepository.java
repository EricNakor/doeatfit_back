package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.CalculatorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalculatorRepository extends JpaRepository<CalculatorEntity, Long> {
    // uuid 찾기
    Optional<CalculatorEntity> findByUuidEntityUuid(UUID uuid);

    // 기록 불러오기
    List<CalculatorEntity> findByUserEntityUserIdOrderByCalcIdDesc(Long userId);

    // 카테고리 값이 0인 것만 불러오기
    Page<CalculatorEntity> findByUserEntityUserIdAndCalcCategoryOrderByCalcIdDesc(Long userId, CalculatorEntity.CalcCategoryEnum calcCategoryEnum, Pageable pageable);

    // 기록 삭제
    void deleteByUuidEntityUuid(UUID uuid);

    // 페이징 처리
    Page<CalculatorEntity> findAllByUserEntityUserId(Long userId, Pageable pageable);

    List<CalculatorEntity> findAllByUserEntityUserId(Long userId);
}
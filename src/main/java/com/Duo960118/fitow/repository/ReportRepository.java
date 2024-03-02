package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.ReportEntity;
import com.Duo960118.fitow.entity.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<ReportEntity, Long>, ReportRepositoryCustom {

    // 삭제
    void deleteByUuidEntityUuid(UUID uuid);

    // 삭제 확인
    boolean existsByUuidEntityUuid(UUID uuid);

    // 문의글 찾기
    Optional<ReportEntity> findByUuidEntityUuid(UUID uuid);

    // 조회 회원의 문의 찾기
    List<ReportEntity> findByUserEntityUserIdOrderByReportIdDesc(Long userId, Pageable pageable);

    // Status 검색 (filter)
    // Category 검색 (filter)
    // email 검색 (String)
    List<ReportEntity> findAllByReportStatusAndReportCategoryAndUserEntityEmail(ReportEntity.ReportStatusEnum reportStatusEnum, ReportEntity.ReportCategoryEnum reportCategoryEnum, String email);
}

package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {
    Page<ReportEntity> findBySearchReportRequest(ReportEntity.ReportStatusEnum reportStatus, ReportEntity.ReportCategoryEnum reportCategory, String email, Pageable pageable);
}

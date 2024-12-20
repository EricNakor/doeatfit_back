package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import org.springframework.data.domain.Page;

public interface ReportRepositoryCustom {
    Page<ReportEntity> findBySearchReportRequest(ReportDto.SearchReportRequestDto searchReportRequestDto);
}

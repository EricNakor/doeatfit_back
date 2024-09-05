package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.ReportDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ReportService {
    // Report 작성
    @Transactional
    ReportDto.PostReportResponseDto postReport(ReportDto.PostReportRequestDto postReportRequest) throws IOException;

    // Report 삭제
    @Transactional
    void deleteReport(UUID uuid);

    // Report 상세내용
    @Transactional
    ReportDto.ReportDetailDto getReportDetail(UUID uuid);

    // 조회 회원의 Report 리스트
    @Transactional
    Page<ReportDto.ReportInfoDto> getReports(ReportDto.GetReportsRequestDto getReportsRequest);

    // admin Report 리스트
    @Transactional
    Page<ReportDto.ReportInfoDto> getAllReport(Pageable pageable);

    // Report 답변
    @Transactional
    ReportDto.ReplyReportResponseDto replyReport(ReportDto.ReplyReportRequestDto replyReportRequest) throws IOException;

    // Status 검색 (filter)
    // Category 검색 (filter)
    // email 검색 (String)
    @Transactional
    Page<ReportDto.ReportInfoDto> searchReport(ReportDto.SearchReportRequestDto searchReportRequest);

    // 신고 및 문의 첨부파일
    @Transactional
    Resource loadReportAttachmentImg(String filename);
    
    // 답변 첨부파일
    @Transactional
    Resource loadReplyAttachmentImg(String filename);

    // 회원 탈퇴 시 외부키 null로 변경
    @Transactional
    void updateForeignKeysNull(Long userId);
}

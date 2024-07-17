package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    // Report 작성
    @Transactional
    UUID postReport(List<MultipartFile> multipartFile, ReportDto.PostReportRequestDto postReportRequest);

    // Report 삭제
    @Transactional
    boolean deleteReport(UUID uuid);

    // Report 상세내용
    @Transactional
    ReportDto.ReportDetailDto getReportDetail(UUID uuid);

    // 조회 회원의 Report 리스트
    @Transactional
    List<ReportDto.ReportInfoDto> getReports(String email, PageRequest pageRequest);

    // admin Report 리스트
    @Transactional
    List<ReportDto.ReportInfoDto> getAllReport(PageRequest pageRequest);

    // Report 답변
    @Transactional
    boolean replyReport(UUID uuid, ReportDto.ReplyReportDto replyReportDto, List<MultipartFile> multipartFile);

    // Status 검색 (filter)
    // Category 검색 (filter)
    // email 검색 (String)
    @Transactional
    Page<ReportDto.ReportInfoDto> searchReport(ReportEntity.ReportStatusEnum reportStatusEnum, ReportEntity.ReportCategoryEnum reportCategoryEnum, String email, Pageable pageable);

    // 신고 및 문의 첨부파일
    @Transactional
    Resource loadReportAttachmentImg(String filename);
    
    // 답변 첨부파일
    @Transactional
    Resource loadReplyAttachmentImg(String filename);

    // 회원 탈퇴 시 외부키 null로 변경
    @Transactional
    void updateForeinKeysNull(Long userId);
}

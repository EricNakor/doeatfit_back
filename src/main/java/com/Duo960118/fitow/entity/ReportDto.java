package com.Duo960118.fitow.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public class ReportDto {
    // 신고 작성 요청
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostReportRequestDto {
        private ReportEntity.ReportCategoryEnum reportCategory;
        private String title;
        private String email;
        private String content;
        private ReportEntity.ReportStatusEnum reportStatus;
        private String reply;
        private List<String> reportFileNames;
        private List<MultipartFile> reportFiles;
    }

    // 신고 작성 응답
    @Getter
    @Builder
    public static class PostReportResponseDto {
        private final ReportEntity.ReportCategoryEnum reportCategory;
        private final String title;
        private final String content;
        private final String email;
        private final List<String> reportFileNames;
        private final ReportEntity.ReportStatusEnum reportStatus;
        private final String reply;
        private final UUID uuid;
    }

    // 신고 detail Service -> viewController
    @Getter
    @RequiredArgsConstructor
    public static class ReportDetailDto {
        private final UUID uuid;
        private final ReportEntity.ReportCategoryEnum reportCategory;
        private final String title;
        private final String email;
        private final String content;
        private final LocalDateTime reportDate;
        private final ReportEntity.ReportStatusEnum reportStatus;
        private final String reply;
        private final LocalDateTime replyDate;
        private final List<String> reportFiles;
        private final List<String> replyFiles;
    }

    // 신고 list
    @Getter
    @RequiredArgsConstructor
    public static class ReportInfoDto {
        private final UUID uuid;
        private final ReportEntity.ReportCategoryEnum reportCategory;
        private final String title;
        private final String email;
        private final LocalDateTime reportDate;
        private final ReportEntity.ReportStatusEnum reportStatus;
        private final LocalDateTime replyDate;
    }

    // 신고 답변 요청
    @Getter
    @Setter
    public static class ReplyReportRequestDto {
        private UUID uuid;
        private ReportEntity.ReportStatusEnum reportStatus;
        private String reply;
        private List<String> replyFileNames;
        private List<MultipartFile> replyFiles;
    }

    // 신고 답변 응답
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ReplyReportResponseDto {
        private final ReportEntity.ReportStatusEnum reportStatus;
        private final String reply;
        private final List<String> replyFiles;
    }

    // 신고 검색
    @Getter
    @Setter
    @Builder
//    @RequiredArgsConstructor
    public static class SearchReportRequestDto {
        private final ReportEntity.ReportStatusEnum reportStatus;
        private final ReportEntity.ReportCategoryEnum reportCategory;
        private final String email;
        private Pageable pageable;
    }

    // 신고 조회
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class GetReportsRequestDto{
        private final String email;
        private final Pageable pageable;
    }
}

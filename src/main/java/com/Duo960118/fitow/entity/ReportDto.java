package com.Duo960118.fitow.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReportDto {
    // 신고 작성
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
        private List<String> reportFiles;

    }

    // 신고 작성 Response
    @Getter
    @RequiredArgsConstructor
    public static class PostReportResponseDto {
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

    // 신고 답변
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ReplyReportDto {
        private final UUID uuid;
        private ReportEntity.ReportStatusEnum reportStatus;
        private String reply;
        private List<String> replyFiles;
    }
}

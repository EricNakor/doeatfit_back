package com.Duo960118.fitow.entity;

import com.Duo960118.fitow.annotaion.Enum;
import com.Duo960118.fitow.annotaion.File;
import jakarta.validation.constraints.*;
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
        @Enum(enumClass = ReportEntity.ReportCategoryEnum.class, message = "{Enum.reportCategory}")
        @NotBlank(message = "{NotBlank.reportCategory}")
        private String reportCategory;
        @Size(min = 5, max = 100, message = "{Size.title}")
        @NotBlank(message = "{NotBlank.title}")
        private String title;
        @Email(message = "{Email.email}")
        private String email;
        @NotBlank(message = "{NotBlank.content}")
        private String content;
        private String reportStatus;
        private String reply;
        private List<String> reportFileNames;
        private List<@File(allowedFileExt = {"jpg", "jpeg", "png"}, fileSizeLimit = 1024 * 1024 * 5) MultipartFile> reportFiles;
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
        @Enum(enumClass = ReportEntity.ReportStatusEnum.class, message = "{Enum.reportStatus}")
        @NotBlank(message = "{NotBlank.reportStatus}")
        private String reportStatus;
        @NotBlank(message = "{NotBlank.reply}")
        private String reply;
        private List<String> replyFileNames;
        private List<@File(allowedFileExt = {"jpg", "jpeg", "png"}, fileSizeLimit = 1024 * 1024 * 5) MultipartFile> replyFiles;
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
        @Enum(enumClass = ReportEntity.ReportStatusEnum.class, message = "{Enum.reportStatus}")
        private final String reportStatus;
        @Enum(enumClass = ReportEntity.ReportCategoryEnum.class, message = "{Enum.reportCategory}")
        private final String reportCategory;
        @Email(message = "{Email.email}")
        private final String email;
        @NotNull(message = "{NotNull.pageable}")
        private Pageable pageable;
    }

    // 신고 조회
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class GetReportsRequestDto{
        @Email(message = "{Email.email}")
        @NotBlank(message = "{NotBlank.email}")
        private final String email;
        @NotNull(message = "{NotNull.pageable}")
        private final Pageable pageable;
    }
}

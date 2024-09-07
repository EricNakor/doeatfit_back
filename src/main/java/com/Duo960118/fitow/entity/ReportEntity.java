package com.Duo960118.fitow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "REPORT")
@EntityListeners(AuditingEntityListener.class)
public class ReportEntity {
    // 신고 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long reportId;
    // 신고 카테고리
    @NotNull(message = "{NotNull.reportCategory}")
    private ReportCategoryEnum reportCategory;
    // 신고자 유저 ID, 이메일
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserEntity userEntity;
    // 신고 제목
    @NotBlank(message = "{NotBlank.title}")
    @Size(min = 5, max = 100, message = "{Size.title}")
    private String title;
    // 신고 일자
    @CreatedDate
    @PastOrPresent(message = "{PastOrPresent.reportDate}")
    private LocalDateTime reportDate;
    // 신고 내용
    @NotBlank(message = "{NotBlank.content}")
    private String Content;
    // 신고 첨부파일
    @ElementCollection
    @CollectionTable(name = "REPORT_ATTACHMENT_FILES", joinColumns = @JoinColumn(name = "REPORT_ID"))
    private List<String> reportFiles;
    // 신고 상태
    @NotNull(message = "{NotNull.reportStatus}")
    private ReportStatusEnum reportStatus;
    // UUID
    @Embedded
    private UuidEntity uuidEntity;
    // 답변
    private String reply;
    // 답변 일자
    @PastOrPresent(message = "{PastOrPresent.replyDate}")
    private LocalDateTime replyDate;
    // 답변 첨부파일
    @ElementCollection
    @CollectionTable(name = "REPLY_ATTACHMENT_FILES", joinColumns = @JoinColumn(name = "REPORT_ID"))
    private List<String> replyFiles;

    // 신고 엔티티 생성자
    @Builder
    public ReportEntity(UserEntity userEntity, long reportId, ReportCategoryEnum reportCategory, String title, String content, List<String> reportFiles, ReportStatusEnum reportStatus, String reply, LocalDateTime replyDate, List<String> replyFiles) {
        this.userEntity = userEntity;
        this.reportId = reportId;
        this.reportCategory = reportCategory;
        this.title = title;
        this.Content = content;
        this.reportFiles = reportFiles;
        this.reportStatus = reportStatus;
        this.reply = reply;
        this.replyDate = replyDate;
        this.replyFiles = replyFiles;
    }

    public void updateUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Getter
    public enum ReportCategoryEnum {
        INQUIRY,
        BUG;

        @JsonCreator
        public static ReportCategoryEnum fromString(String str) {
            if(str.isEmpty()){
                return null;
            }
            return ReportCategoryEnum.valueOf(str);
        }
    }

    @Getter
    public enum ReportStatusEnum {
        TODO,
        IN_PROGRESS,
        RESOLVED;

        @JsonCreator
        public static ReportStatusEnum fromString(String str) {
            if(str.isEmpty()){
                return null;
            }
            return ReportStatusEnum.valueOf(str);
        }
    }

    public void replyReport(ReportDto.ReplyReportRequestDto replyReportRequest) {
        if (!this.reply.equals(replyReportRequest.getReply())) {
            this.replyDate = LocalDateTime.now();
            this.reply = replyReportRequest.getReply();
            this.replyFiles = replyReportRequest.getReplyFileNames();
        }
        this.reportStatus = ReportStatusEnum.fromString(replyReportRequest.getReportStatus());
    }
}

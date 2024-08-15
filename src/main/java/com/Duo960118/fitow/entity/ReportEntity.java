package com.Duo960118.fitow.entity;

import jakarta.persistence.*;
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
    private ReportCategoryEnum reportCategory;
    // 신고자 유저 ID, 이메일
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserEntity userEntity;
    // 신고 제목
    private String title;
    // 신고 일자
    @CreatedDate
    private LocalDateTime reportDate;
    // 신고 내용
    private String Content;
    // 신고 첨부파일
    @ElementCollection
    @CollectionTable(name = "REPORT_ATTACHMENT_FILES", joinColumns = @JoinColumn(name = "REPORT_ID"))
    private List<String> reportFiles;
    // 신고 상태
    private ReportStatusEnum reportStatus;
    // UUID
    @Embedded
    private UuidEntity uuidEntity;
    // 답변
    private String reply;
    // 답변 일자
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
        BUG
    }

    @Getter
    public enum ReportStatusEnum {
        TODO,
        IN_PROGRESS,
        RESOLVED
    }

    public void replyReport(ReportDto.ReplyReportRequestDto replyReport) {
        if (!this.reply.equals(replyReport.getReply())) {
            this.replyDate = LocalDateTime.now();
            this.reply = replyReport.getReply();
            this.replyFiles = replyReport.getReplyFileNames();
        }
        this.reportStatus = replyReport.getReportStatus();
    }
}

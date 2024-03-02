package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.ReportDto;
import com.Duo960118.fitow.entity.ReportEntity;
import org.springframework.context.annotation.Bean;

public class ReportMapper {
    @Bean
    public static ReportDto.ReportInfoDto entityToReportInfoDto(ReportEntity reportEntity) {
        return new ReportDto.ReportInfoDto(
                reportEntity.getUuidEntity().getUuid(),
                reportEntity.getReportCategory(),
                reportEntity.getTitle(),
                reportEntity.getUserEntity().getEmail(),
                reportEntity.getReportDate(),
                reportEntity.getReportStatus(),
                reportEntity.getReplyDate()
        );
    }
}

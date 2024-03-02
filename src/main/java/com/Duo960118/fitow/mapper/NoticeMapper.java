package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import org.springframework.context.annotation.Bean;

public class NoticeMapper {
    @Bean
    public static NoticeDto.NoticeInfoDto entityToNoticeInfoDto(NoticeEntity noticeEntity){

        return new NoticeDto.NoticeInfoDto(noticeEntity.getUuidEntity().getUuid(),
                noticeEntity.getTitle(),noticeEntity.getNoticeCategory(),
                noticeEntity.getUserEntity().getNickName(),noticeEntity.getCreatedAt());
    }
}

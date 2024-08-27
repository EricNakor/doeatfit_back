package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import org.springframework.context.annotation.Bean;

public class NoticeMapper {
    @Bean
    public static NoticeDto.NoticeInfoDto entityToNoticeInfoDto(NoticeEntity noticeEntity){
        // 작성자가 탈퇴한 경우
        if(noticeEntity.getUserEntity() == null){
            return new NoticeDto.NoticeInfoDto(noticeEntity.getUuidEntity().getUuid(),
                    noticeEntity.getTitle(),noticeEntity.getNoticeCategory(),
                    "탈퇴한 사용자",noticeEntity.getCreatedAt());
        }
        return new NoticeDto.NoticeInfoDto(noticeEntity.getUuidEntity().getUuid(),
                noticeEntity.getTitle(),noticeEntity.getNoticeCategory(),
                noticeEntity.getUserEntity().getNickName(),noticeEntity.getCreatedAt());
    }
}

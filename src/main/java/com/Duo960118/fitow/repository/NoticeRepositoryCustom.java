package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import org.springframework.data.domain.Page;

public interface NoticeRepositoryCustom {
    Page<NoticeEntity> findBySearchNoticeRequest(NoticeDto.SearchNoticeRequestDto searchNoticeRequest);
}

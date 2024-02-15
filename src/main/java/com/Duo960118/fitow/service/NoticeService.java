package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface NoticeService {
    // Notice 추가
    @Transactional
    UUID postNotice(NoticeDto.PostNoticeRequestDto postNotice );

    // Notice 삭제
    @Transactional
    boolean deleteNotice(UUID uuid);
    
    // Notice 수정
    @Transactional
    boolean editNotice(UUID uuid,NoticeDto.PostNoticeRequestDto editNoticeRequest );

    // notice 세부 사항
    @Transactional
    NoticeDto.NoticeDetailDto getNoticeDetail(UUID uuid);

    // 조건에 맞는 Notice 검색
    @Transactional
    List<NoticeDto.NoticeInfoDto> searchNotice(NoticeEntity.NoticeCategoryEnum category, String searchString);

    // 모든 Notice를 정렬된 상태로 반환
    @Transactional
    List<NoticeDto.NoticeInfoDto> getNotices(Sort sort);

    // PageRequest에 맞게 Notice들을 반환
    @Transactional
    List<NoticeDto.NoticeInfoDto> getNoticePage(PageRequest pageRequest);
}

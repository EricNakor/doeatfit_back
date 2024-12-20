package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.NoticeDto;
import com.Duo960118.fitow.entity.NoticeEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.Duo960118.fitow.entity.QNoticeEntity.noticeEntity;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    protected final Log logger = LogFactory.getLog(this.getClass());

    //
    @Override
    public Page<NoticeEntity> findBySearchNoticeRequest(NoticeDto.SearchNoticeRequestDto searchNoticeRequest) {

        List<NoticeEntity> fetch = queryFactory
                .select(noticeEntity)
                .from(noticeEntity)
                .where(containsNoticeName(searchNoticeRequest.getSearchString()))
                .where(eqNoticeCategory(NoticeEntity.NoticeCategoryEnum.fromJson(searchNoticeRequest.getCategory())))
                .offset(searchNoticeRequest.getPageable().getOffset())
                .limit(searchNoticeRequest.getPageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = getCount(searchNoticeRequest);

        return PageableExecutionUtils.getPage(fetch, searchNoticeRequest.getPageable(), countQuery::fetchOne);
    }

    // 개수 세는 용도? 왜쓰는지 모름. 페이징할 때 필요하다는 것만 암
    private JPAQuery<Long> getCount(NoticeDto.SearchNoticeRequestDto searchNoticeRequest) {

        return queryFactory
                .select(noticeEntity.count())
                .from(noticeEntity)
                .where(eqNoticeCategory(NoticeEntity.NoticeCategoryEnum.fromJson(searchNoticeRequest.getCategory())));
    }

    // 공지 제목 검색
    private BooleanExpression containsNoticeName(String searchString) {
        if (!StringUtils.hasLength(searchString)) {
            return null;
        }
        return noticeEntity.title.contains(searchString);
    }

    // 공지 카테고리 검색
    private BooleanExpression eqNoticeCategory(NoticeEntity.NoticeCategoryEnum noticeCategoryEnum) {
        if (ObjectUtils.isEmpty(noticeCategoryEnum)) {
            return null;
        }
        return noticeEntity.noticeCategory.eq(noticeCategoryEnum);
    }
}
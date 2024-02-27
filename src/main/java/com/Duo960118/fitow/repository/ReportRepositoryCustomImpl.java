package com.Duo960118.fitow.repository;

import static com.Duo960118.fitow.entity.QReportEntity.reportEntity;

import com.Duo960118.fitow.entity.ReportEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;


@RequiredArgsConstructor
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportEntity> findBySearchReportRequest(ReportEntity.ReportStatusEnum reportStatus, ReportEntity.ReportCategoryEnum reportCategory, String email, Pageable pageable) {
        List<ReportEntity> fetch = queryFactory
                .select(reportEntity)
                .from(reportEntity)
                .where(eqReportStatus(reportStatus))
                .where(eqReportCategory(reportCategory))
                .where(containsEmail(email))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = getCount(reportStatus, reportCategory, email);

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getCount(ReportEntity.ReportStatusEnum reportStatus, ReportEntity.ReportCategoryEnum reportCategory, String email) {
        return queryFactory
                .select(reportEntity.count())
                .from(reportEntity)
                .where(eqReportStatus(reportStatus))
                .where(eqReportCategory(reportCategory))
                .where(containsEmail(email));
    }

    // email 검색
    private BooleanExpression containsEmail(String email) {
        if (!StringUtils.hasLength(email)) {
            return null;
        }
        return reportEntity.userEntity.email.contains(email);
    }

    private BooleanExpression eqReportStatus(ReportEntity.ReportStatusEnum reportStatus) {
        if (ObjectUtils.isEmpty(reportStatus)) {
            return null;
        }
        return reportEntity.reportStatus.eq(reportStatus);
    }

    private BooleanExpression eqReportCategory(ReportEntity.ReportCategoryEnum reportCategory) {
        if (ObjectUtils.isEmpty(reportCategory)) {
            return null;
        }
        return reportEntity.reportCategory.eq(reportCategory);
    }
}

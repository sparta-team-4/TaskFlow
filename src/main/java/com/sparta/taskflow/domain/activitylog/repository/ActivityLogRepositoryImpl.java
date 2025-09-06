package com.sparta.taskflow.domain.activitylog.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import com.sparta.taskflow.domain.activitylog.entity.QActivityLog;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActivityLogRepositoryImpl implements ActivityLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ActivityLog> search(ActivityType type, Long taskId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        QActivityLog activityLog = QActivityLog.activityLog;

        BooleanBuilder builder = new BooleanBuilder();

        if (type != null) builder.and(activityLog.type.eq(type));
        if (taskId != null) builder.and(activityLog.taskId.eq(taskId));
        if (startDate != null) builder.and(activityLog.createdAt.goe(startDate));
        if (endDate != null) builder.and(activityLog.createdAt.loe(endDate));

        List<ActivityLog> content = jpaQueryFactory
                .selectFrom(activityLog)
                .where(builder)
                .orderBy(activityLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(jpaQueryFactory
                        .select(activityLog.count())
                        .from(activityLog)
                        .where(builder)
                        .fetchOne())
                .orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}

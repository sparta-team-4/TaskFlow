package com.sparta.taskflow.domain.task.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.taskflow.domain.task.entity.QTask;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.repository.dto.DailyTaskProjection;
import com.sparta.taskflow.domain.task.repository.dto.QDailyTaskProjection;
import com.sparta.taskflow.domain.task.repository.dto.QTaskStatisticsProjection;
import com.sparta.taskflow.domain.task.repository.dto.TaskStatisticsProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.dateTemplate;

@Slf4j
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public TaskStatisticsProjection findDashboardStats(Long userId) {
        /*
        select
            count(*) as totaltasks,
            sum(case when status = 'done' then 1 else 0 end) as completedtasks,
            sum(case when status = 'in_progress' then 1 else 0 end) as inprogresstasks,
            sum(case when status = 'todo' then 1 else 0 end) as todotasks,
            sum(case when status <> 'done' and due_date < now() then 1 else 0 end) as overduetasks,
            sum(case when status <> 'done' and user_id = 1 then 1 else 0 end) as myTasksToday,
            sum(case when status = 'done' and user_id = 1 then 1 else 0 end) as myDoneTasks
        from task
        where is_deleted = false;
         */
        QTask task = QTask.task;

        return jpaQueryFactory
                .select(
                        new QTaskStatisticsProjection(
                                task.count().intValue(),
                                new CaseBuilder().when(task.status.eq(TaskStatus.DONE)).then(1).otherwise(0).sum(),
                                new CaseBuilder().when(task.status.eq(TaskStatus.IN_PROGRESS)).then(1).otherwise(0).sum(),
                                new CaseBuilder().when(task.status.eq(TaskStatus.TODO)).then(1).otherwise(0).sum(),
                                new CaseBuilder()
                                        .when(task.status.ne(TaskStatus.DONE).and(task.dueDate.lt(LocalDateTime.now())))
                                        .then(1)
                                        .otherwise(0).sum(),
                                new CaseBuilder()
                                        .when(task.status.ne(TaskStatus.DONE).and(task.assignee.id.eq(userId)))
                                        .then(1)
                                        .otherwise(0).sum(),
                                new CaseBuilder()
                                        .when(task.status.eq(TaskStatus.DONE).and(task.assignee.id.eq(userId)))
                                        .then(1)
                                        .otherwise(0).sum()
                        )
                )
                .from(task)
//                .where(task.isDeleted.isFalse())
                .fetchOne();
    }

    @Override
    public List<DailyTaskProjection> findWeeklyTaskSummary(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        QTask task = QTask.task;

        // LocalDate로 캐스팅
        DateTemplate<LocalDate> dueDateOnly = Expressions.dateTemplate(
                LocalDate.class,
                "cast({0} as LocalDate)",
                task.dueDate
        );
        NumberExpression<Integer> completedSum = new CaseBuilder()
                .when(task.status.eq(TaskStatus.DONE))
                .then(1)
                .otherwise(0).sum();

        return jpaQueryFactory
                .select(
                        new QDailyTaskProjection(
                                dueDateOnly,
                                task.count().intValue(),
                                completedSum
                        )
                )
                .from(task)
                .where(
                        task.dueDate.goe(startDateTime),
                        task.dueDate.lt(endDateTime)
                )
                .groupBy(dueDateOnly)
                .orderBy(dueDateOnly.asc())
                .fetch();
    }
}

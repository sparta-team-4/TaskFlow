package com.sparta.taskflow.domain.task.repository;

import com.sparta.taskflow.domain.task.repository.dto.TaskStatisticsProjection;
import com.sparta.taskflow.domain.task.repository.dto.DailyTaskProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskQueryRepository {

    TaskStatisticsProjection findDashboardStats(Long userId);

    List<DailyTaskProjection> findWeeklyTaskSummary(LocalDateTime startDateTime, LocalDateTime endDateTime);
}

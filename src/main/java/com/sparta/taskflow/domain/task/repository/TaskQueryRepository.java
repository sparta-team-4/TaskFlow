package com.sparta.taskflow.domain.task.repository;

import com.sparta.taskflow.domain.task.repository.dto.TaskStatisticsProjection;

public interface TaskQueryRepository {

    TaskStatisticsProjection findDashboardStats(Long userId);
}

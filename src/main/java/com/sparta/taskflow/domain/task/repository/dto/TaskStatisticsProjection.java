package com.sparta.taskflow.domain.task.repository.dto;

import com.querydsl.core.annotations.QueryProjection;

public record TaskStatisticsProjection(
        int totalTasks,
        int completedTasks,
        int inProgressTasks,
        int todoTasks,
        int overdueTasks,
        int myTasksToday,
        int myDoneTasks
) {
    @QueryProjection
    public TaskStatisticsProjection {
    }
}

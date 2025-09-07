package com.sparta.taskflow.domain.dashboard.dto.response;

import com.sparta.taskflow.domain.task.repository.dto.TaskStatisticsProjection;
import lombok.Builder;

@Builder
public record TaskStatisticsResponse(
        int totalTasks,
        int completedTasks,
        int inProgressTasks,
        int todoTasks,
        int overdueTasks,
        int teamProgress,
        int myTasksToday,
        int completionRate
) {

    public static TaskStatisticsResponse from(TaskStatisticsProjection dashboardStats, double teamProgress, double completionRate) {
        return TaskStatisticsResponse.builder()
                .totalTasks(dashboardStats.totalTasks())
                .completedTasks(dashboardStats.completedTasks())
                .inProgressTasks(dashboardStats.inProgressTasks())
                .todoTasks(dashboardStats.todoTasks())
                .overdueTasks(dashboardStats.overdueTasks())
                .teamProgress((int) teamProgress)
                .myTasksToday(dashboardStats.myTasksToday())
                .completionRate((int) completionRate)
                .build();
    }
}

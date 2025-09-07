package com.sparta.taskflow.domain.dashboard.service;

import com.sparta.taskflow.domain.dashboard.dto.response.TaskStatisticsResponse;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import com.sparta.taskflow.domain.task.repository.dto.TaskStatisticsProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardQueryService {

    private static final int PERCENTAGE = 100;
    private static final double ZERO_PERCENT = 0.0;

    private final TaskRepository taskRepository;

    public TaskStatisticsResponse getDashboardStatistics(Long userId) {
        TaskStatisticsProjection dashboardStats = taskRepository.findDashboardStats(userId);

        int totalTasks = dashboardStats.totalTasks();
        int completedTasks = dashboardStats.completedTasks();
        int myDoneTasks = dashboardStats.myDoneTasks();

        return TaskStatisticsResponse.from(
                dashboardStats,
                getProgress(totalTasks, completedTasks),
                getProgress(totalTasks, myDoneTasks)
        );
    }

    private double getProgress(int totalTasks, int completedTasks) {
        return totalTasks == ZERO_PERCENT ? ZERO_PERCENT : ((double) completedTasks / totalTasks) * PERCENTAGE;
    }

}


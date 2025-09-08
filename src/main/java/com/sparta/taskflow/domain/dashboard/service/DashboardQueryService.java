package com.sparta.taskflow.domain.dashboard.service;

import com.sparta.taskflow.domain.dashboard.dto.response.DashboardResponseDto;
import com.sparta.taskflow.domain.dashboard.dto.response.MyTaskSummaryResponse;
import com.sparta.taskflow.domain.dashboard.dto.response.TaskStatisticsResponse;
import com.sparta.taskflow.domain.task.dto.TaskResponse;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import com.sparta.taskflow.domain.task.repository.dto.TaskStatisticsProjection;
import com.sparta.taskflow.domain.task.repository.dto.TeamProgressProjection;
import com.sparta.taskflow.domain.task.service.TaskInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardQueryService {

    private static final int PERCENTAGE = 100;
    private static final double ZERO_PERCENT = 0.0;

    private final TaskRepository taskRepository;
    private final TaskInternalService taskInternalService;


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

    public MyTaskSummaryResponse getMyTasksSummary(Long assigneeId) {
        List<Task> tasks = taskRepository.findAllByAssigneeId(assigneeId);

        List<TaskResponse> todayTasks = new ArrayList<>();
        List<TaskResponse> upcomingTasks = new ArrayList<>();
        List<TaskResponse> overdueTasks = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        for (Task task : tasks) {
            LocalDateTime dueDate = task.getDueDate();

            if (task.getStatus().equals(TaskStatus.DONE)) {
                continue;
            }

            if (dueDate.isBefore(now)) {
                overdueTasks.add(TaskResponse.create(task));
            } else if (dueDate.toLocalDate().isEqual(today)) {
                todayTasks.add(TaskResponse.create(task));
            } else {
                upcomingTasks.add(TaskResponse.create(task));
            }
        }
        return MyTaskSummaryResponse.of(todayTasks, upcomingTasks, overdueTasks);
    }

    private double getProgress(int totalTasks, int completedTasks) {
        return totalTasks == ZERO_PERCENT ? ZERO_PERCENT : ((double) completedTasks / totalTasks) * PERCENTAGE;
    }

    public DashboardResponseDto getTeamProgress() {
        List<TeamProgressProjection> projections = taskInternalService.findTeamProgress();
        return DashboardResponseDto.from(projections);
    }

}


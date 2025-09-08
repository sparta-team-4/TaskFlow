package com.sparta.taskflow.domain.dashboard.service;

import com.sparta.taskflow.domain.dashboard.dto.response.DashboardResponseDto;
import com.sparta.taskflow.domain.dashboard.dto.response.MyTaskSummaryResponse;
import com.sparta.taskflow.domain.dashboard.dto.response.TaskStatisticsResponse;
import com.sparta.taskflow.domain.task.dto.TaskResponse;
import com.sparta.taskflow.domain.dashboard.dto.response.WeeklyTrendResponse;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import com.sparta.taskflow.domain.task.repository.dto.DailyTaskProjection;
import com.sparta.taskflow.domain.task.repository.dto.TaskStatisticsProjection;
import com.sparta.taskflow.domain.task.repository.dto.TeamProgressProjection;
import com.sparta.taskflow.domain.task.service.TaskInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardQueryService {

    private static final int PERCENTAGE = 100;
    private static final double ZERO_PERCENT = 0.0;

    private static final int FIRST_DAY_INDEX = 0;
    private static final int DAYS_IN_WEEK = 7;

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

    public DashboardResponseDto getTeamProgress() {
        List<TeamProgressProjection> projections = taskInternalService.findTeamProgress();
        return DashboardResponseDto.from(projections);
    }

    public List<WeeklyTrendResponse> getWeeklyTrend() {
        LocalDate today = LocalDate.of(2025, 9, 12);
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        LocalDateTime startDateTime = monday.atStartOfDay();
        LocalDateTime endDateTime = sunday.plusDays(1).atStartOfDay();

        Map<LocalDate, DailyTaskProjection> summaryMap = getDailTaskSummary(startDateTime, endDateTime);

        return IntStream.range(FIRST_DAY_INDEX, DAYS_IN_WEEK)
                .mapToObj(i -> getWeeklyTrendResponse(monday.plusDays(i), summaryMap))
                .toList();
    }

    private double getProgress(int totalTasks, int completedTasks) {
        return totalTasks == ZERO_PERCENT ? ZERO_PERCENT : ((double) completedTasks / totalTasks) * PERCENTAGE;
    }

    private Map<LocalDate, DailyTaskProjection> getDailTaskSummary(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return taskRepository.findWeeklyTaskSummary(startDateTime, endDateTime).stream()
                .collect(Collectors.toMap(DailyTaskProjection::date, dailyTask -> dailyTask));
    }
    private WeeklyTrendResponse getWeeklyTrendResponse(LocalDate date, Map<LocalDate, DailyTaskProjection> summaryMap) {
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA);
        DailyTaskProjection dailyTask = summaryMap.getOrDefault(date, DailyTaskProjection.empty(date));

        return WeeklyTrendResponse.of(
                dayName,
                dailyTask.totalTasks(),
                dailyTask.completedTasks(),
                date
        );
    }

}


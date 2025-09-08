package com.sparta.taskflow.domain.dashboard.dto.response;

import com.sparta.taskflow.domain.task.dto.TaskResponse;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MyTaskSummaryResponse {

    private final List<TaskResponse> todayTasks;
    private final List<TaskResponse> upcomingTasks;
    private final List<TaskResponse> overdueTasks;

    @Builder
    private MyTaskSummaryResponse(List<TaskResponse> todayTasks, List<TaskResponse> upcomingTasks, List<TaskResponse> overdueTasks) {
        this.todayTasks = todayTasks;
        this.upcomingTasks = upcomingTasks;
        this.overdueTasks = overdueTasks;
    }

    public static MyTaskSummaryResponse of(List<TaskResponse> todayTasks, List<TaskResponse> upcomingTasks, List<TaskResponse> overdueTasks) {
        return MyTaskSummaryResponse.builder()
                .todayTasks(todayTasks)
                .upcomingTasks(upcomingTasks)
                .overdueTasks(overdueTasks)
                .build();
    }

    @Builder
    public record MyTaskSummary(Long id, String title, TaskStatus status, LocalDateTime dueDate) {
        public static MyTaskSummary from(Task task) {
            return MyTaskSummary.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .status(task.getStatus())
                    .dueDate(task.getDueDate())
                    .build();
        }
    }

}

package com.sparta.taskflow.domain.dashboard.dto.response;

import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MyTaskSummaryResponse {

    private final List<MyTaskSummary> todayTasks;
    private final List<MyTaskSummary> upcomingTasks;
    private final List<MyTaskSummary> overdueTasks;

    @Builder
    private MyTaskSummaryResponse(List<MyTaskSummary> todayTasks, List<MyTaskSummary> upcomingTasks, List<MyTaskSummary> overdueTasks) {
        this.todayTasks = todayTasks;
        this.upcomingTasks = upcomingTasks;
        this.overdueTasks = overdueTasks;
    }

    public static MyTaskSummaryResponse of(List<MyTaskSummary> todayTasks, List<MyTaskSummary> upcomingTasks, List<MyTaskSummary> overdueTasks) {
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

package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskPriority;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final TaskPriority priority;
    private final TaskStatus status;
    private final LocalDateTime dueDate;
    private final Long assigneeId;

    @Builder
    private TaskResponse(Long id, String title, String description,
                         TaskPriority priority, TaskStatus status,
                         LocalDateTime dueDate, Long assigneeId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.assigneeId = assigneeId;
    }

    public static TaskResponse create(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .assigneeId(task.getAssignee().getId())
                .build();
    }
}


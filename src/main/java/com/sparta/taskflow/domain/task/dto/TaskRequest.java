package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.enums.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskPriority priority;
    private Long assigneeId;
}

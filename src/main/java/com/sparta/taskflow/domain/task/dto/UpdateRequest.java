package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.enums.TaskPriority;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpdateRequest {
    private String title;
    private String description;
    @FutureOrPresent(message = "dueDate는 현재 시각 이후여야 합니다.")
    private LocalDateTime dueDate;
    private TaskPriority priority;
    private TaskStatus status;
    private Long assigneeId;
}

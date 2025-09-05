package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.enums.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TaskRequest {
    @NotBlank(message = "title은 비워둘 수 없습니다.")
    private String title;
    @NotBlank(message = "description은 비워둘 수 없습니다.")
    private String description;
    @NotNull(message = "dueDate는 필수입니다.")
    @FutureOrPresent(message = "dueDate는 현재 시각 이후여야 합니다.")
    private LocalDateTime dueDate;
    @NotNull(message = "priority는 필수입니다.")
    private TaskPriority priority;
    @NotNull(message = "assigneeId는 필수입니다.")
    private Long assigneeId;
}

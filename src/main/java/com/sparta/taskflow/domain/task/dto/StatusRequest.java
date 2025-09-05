package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusRequest {
    @NotNull(message = "status는 필수입니다.")
    private TaskStatus status;
}

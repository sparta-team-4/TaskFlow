package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TaskResponse {
    private final Task data;

    @Builder
    private TaskResponse(Task data) {
        this.data = data;
    }

    public static TaskResponse create(Task data){
        return TaskResponse.builder()
                .data(data)
                .build();
    }
}

package com.sparta.taskflow.domain.task.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class CategoryResponse {
    private boolean success;
    private String message;
    private final DataDto content;
    private LocalDateTime timestamp;

    @Builder
    private CategoryResponse(boolean success,
                        String message,
                        DataDto content,
                        LocalDateTime timestamp) {
        this.content = content;
    }

    public static CategoryResponse getByCategoryResponse(DataDto content){
        return CategoryResponse.builder()
                .success(true)
                .message("Task 목록을 조회했습니다.")
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

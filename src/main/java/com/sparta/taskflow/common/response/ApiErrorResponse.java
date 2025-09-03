package com.sparta.taskflow.common.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiErrorResponse {

    private final boolean success;
    private final String message;
    private final Object data;
    private final LocalDateTime timestamp;

    @Builder
    private ApiErrorResponse(String message) {
        this.success = false;
        this.message = message;
        this.data = null;
        this.timestamp = LocalDateTime.now();
    }

    public static ApiErrorResponse from(String message) {
        return new ApiErrorResponse(message);
    }

}

package com.sparta.taskflow.domain.task.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TaskErrorCode implements ErrorCode {

    DELETED_TASK(HttpStatus.NOT_FOUND, "이미 삭제된 Task 입니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 Task 입니다");

    private final HttpStatus httpStatus;
    private final String message;
}
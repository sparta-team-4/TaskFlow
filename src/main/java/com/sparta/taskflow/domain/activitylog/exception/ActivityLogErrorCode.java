package com.sparta.taskflow.domain.activitylog.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ActivityLogErrorCode implements ErrorCode {

    ACTIVITY_LOG_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "존재하지 않는 활동 로그입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.sparta.taskflow.common.logs.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LogErrorCode implements ErrorCode {

    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "유효한 인증 정보가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

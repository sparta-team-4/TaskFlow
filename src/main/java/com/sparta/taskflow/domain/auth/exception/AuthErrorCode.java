package com.sparta.taskflow.domain.auth.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 사용자명 또는 비밀번호입니다"),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자명입니다");

    private final HttpStatus httpStatus;
    private final String message;

}

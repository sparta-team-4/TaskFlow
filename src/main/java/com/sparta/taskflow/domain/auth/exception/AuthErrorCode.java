package com.sparta.taskflow.domain.auth.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "존재하지 않는 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED, "블랙리스트된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰 형식입니다."),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "이메일이 잘못되었습니다."),
    INVALID_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 사용자명 또는 비밀번호입니다"),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자명입니다"),

    // 토큰 데이터가 없을 때 400(Bad Request)
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "JWT 토큰이 필요합니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

package com.sparta.taskflow.common.exception.server;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServerErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    TOKEN_ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 암호화에 실패하였습니다."),
    TOKEN_DECRYPTION_FAILED(HttpStatus.UNAUTHORIZED, "토큰 복호화에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

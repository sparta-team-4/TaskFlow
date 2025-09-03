package com.sparta.taskflow.domain.sample.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SampleErrorCode implements ErrorCode {

    SAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않은 Sample입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.sparta.taskflow.domain.task.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class CustomException extends GlobalException {

    public CustomException(ErrorCode errorCode) {
        super(errorCode);
    }
}

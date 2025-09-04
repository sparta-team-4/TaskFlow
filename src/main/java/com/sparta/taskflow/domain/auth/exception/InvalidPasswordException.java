package com.sparta.taskflow.domain.auth.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class InvalidPasswordException extends GlobalException {

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}

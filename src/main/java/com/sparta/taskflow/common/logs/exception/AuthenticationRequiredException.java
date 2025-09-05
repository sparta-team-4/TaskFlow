package com.sparta.taskflow.common.logs.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class AuthenticationRequiredException extends GlobalException {

    public AuthenticationRequiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}

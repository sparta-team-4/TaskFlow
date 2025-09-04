package com.sparta.taskflow.domain.auth.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class InvalidUsernameOrPasswordException extends GlobalException {

    public InvalidUsernameOrPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}

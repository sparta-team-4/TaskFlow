package com.sparta.taskflow.domain.user.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class UserNotFoundException extends GlobalException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

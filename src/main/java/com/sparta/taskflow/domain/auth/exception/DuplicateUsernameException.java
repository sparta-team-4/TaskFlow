package com.sparta.taskflow.domain.auth.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class DuplicateUsernameException extends GlobalException {

    public DuplicateUsernameException(ErrorCode errorCode) {
        super(errorCode);
    }
}

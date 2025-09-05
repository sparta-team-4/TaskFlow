package com.sparta.taskflow.domain.auth.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class DuplicateEmailException extends GlobalException {

    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}

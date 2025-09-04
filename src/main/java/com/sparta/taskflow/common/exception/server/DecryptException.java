package com.sparta.taskflow.common.exception.server;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class DecryptException extends GlobalException {

    public DecryptException(ErrorCode errorCode) {
        super(errorCode);
    }
}
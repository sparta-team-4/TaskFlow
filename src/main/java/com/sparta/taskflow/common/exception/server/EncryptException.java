package com.sparta.taskflow.common.exception.server;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class EncryptException extends GlobalException {

    public EncryptException(ErrorCode errorCode) {
        super(errorCode);
    }
}
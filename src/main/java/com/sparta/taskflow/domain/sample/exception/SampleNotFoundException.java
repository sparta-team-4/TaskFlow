package com.sparta.taskflow.domain.sample.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class SampleNotFoundException extends GlobalException {

    public SampleNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

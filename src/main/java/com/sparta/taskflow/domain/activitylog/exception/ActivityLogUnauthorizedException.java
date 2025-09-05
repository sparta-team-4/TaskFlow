package com.sparta.taskflow.domain.activitylog.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class ActivityLogUnauthorizedException extends GlobalException {

    public ActivityLogUnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}

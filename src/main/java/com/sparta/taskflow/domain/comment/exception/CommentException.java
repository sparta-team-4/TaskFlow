package com.sparta.taskflow.domain.comment.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class CommentException extends GlobalException {

    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}

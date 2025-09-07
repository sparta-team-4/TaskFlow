package com.sparta.taskflow.domain.team.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class MemberAlreadyExistsException extends GlobalException {

  public MemberAlreadyExistsException(ErrorCode errorCode) {
    super(errorCode);
  }
}
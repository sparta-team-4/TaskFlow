package com.sparta.taskflow.domain.team.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class TeamNotFoundException extends GlobalException {

  public TeamNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}

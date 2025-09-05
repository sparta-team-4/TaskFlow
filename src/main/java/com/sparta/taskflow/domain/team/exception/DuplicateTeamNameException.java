package com.sparta.taskflow.domain.team.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class DuplicateTeamNameException extends GlobalException {

  public DuplicateTeamNameException(ErrorCode errorCode) {
    super(errorCode);
  }
}
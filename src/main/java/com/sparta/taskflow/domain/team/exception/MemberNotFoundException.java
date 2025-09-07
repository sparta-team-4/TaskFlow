package com.sparta.taskflow.domain.team.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import com.sparta.taskflow.common.exception.GlobalException;

public class MemberNotFoundException extends GlobalException {

  public MemberNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
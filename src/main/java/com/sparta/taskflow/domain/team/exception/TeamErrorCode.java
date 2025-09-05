package com.sparta.taskflow.domain.team.exception;

import com.sparta.taskflow.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamErrorCode implements ErrorCode {

    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    DUPLICATE_TEAM_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 팀 이름입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 팀 멤버입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자는 팀의 멤버가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

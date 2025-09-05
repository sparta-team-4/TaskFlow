package com.sparta.taskflow.domain.activitylog.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
    TASK_CREATED("새로운 작업 %s이 생성되었습니다."),
    TASK_UPDATED("작업 정보를 수정했습니다."),
    TASK_DELETED("작업을 삭제했습니다."),
    TASK_STATUS_CHANGED("작업 상태가 %s에서 %s로 변경되었습니다."),
    COMMENT_CREATED("작업에 댓글을 작성했습니다."),
    COMMENT_UPDATED("댓글을 수정했습니다."),
    COMMENT_DELETED("댓글을 삭제했습니다.");

    private final String description;

    public String getDescription(Object... args) {
        return String.format(description, args);
    }
}

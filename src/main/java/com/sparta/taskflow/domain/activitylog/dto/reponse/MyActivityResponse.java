package com.sparta.taskflow.domain.activitylog.dto.reponse;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.user.dto.response.UserIdAndNameResponse;
import com.sparta.taskflow.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MyActivityResponse(
        Long id,
        Long userId,
        UserIdAndNameResponse userIdAndNameResponse,
        ActivityType action,
        String target,
        Long targetId,
        String description,
        LocalDateTime createdAt
) {
    public static MyActivityResponse from(ActivityLog activityLog) {
        User user = activityLog.getUser();
        return MyActivityResponse.builder()
                .id(activityLog.getId())
                .userId(user.getId())
                .userIdAndNameResponse(UserIdAndNameResponse.from(user))
                .action(activityLog.getType())
                .target(activityLog.getType().getTarget())
                .targetId(activityLog.getTaskId())
                .description(activityLog.getDescription())
                .createdAt(activityLog.getCreatedAt())
                .build();
    }
}

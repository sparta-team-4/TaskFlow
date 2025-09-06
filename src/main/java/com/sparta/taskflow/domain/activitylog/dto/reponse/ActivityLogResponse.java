package com.sparta.taskflow.domain.activitylog.dto.reponse;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.user.dto.response.UserDetailsResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ActivityLogResponse(

        Long id,
        ActivityType type,
        Long userId,
        UserDetailsResponse user,
        Long taskId,
        LocalDateTime timestamp,
        String description
    ) {
    public static ActivityLogResponse from(ActivityLog activityLog) {
        UserDetailsResponse userRegisterResponse = UserDetailsResponse.from(activityLog.getUser());

        return ActivityLogResponse.builder()
                .id(activityLog.getId())
                .type(activityLog.getType())
                .userId(userRegisterResponse.id())
                .user(userRegisterResponse)
                .taskId(activityLog.getTaskId())
                .timestamp(activityLog.getCreatedAt())
                .description(activityLog.getDescription())
                .build();
    }
}

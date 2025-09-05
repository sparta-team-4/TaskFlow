package com.sparta.taskflow.domain.activitylog.dto.reponse;

import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.auth.dto.response.UserRegisterResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ActivityLogResponse(

        Long id,
        ActivityType type,
        Long userId,
        UserRegisterResponse user,
        Long taskId,
        LocalDateTime timestamp,
        String description
    ) {}

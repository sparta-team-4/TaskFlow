package com.sparta.taskflow.domain.user.dto.response;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserIdAndNameResponse(Long id, String username) {

    public static UserIdAndNameResponse from(User user) {
        return UserIdAndNameResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}

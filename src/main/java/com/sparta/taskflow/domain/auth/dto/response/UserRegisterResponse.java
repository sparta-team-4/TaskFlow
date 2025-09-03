package com.sparta.taskflow.domain.auth.dto.response;


import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserRegisterResponse(Long id, String username, String email, String name, Role role, LocalDateTime createdAt) {

    public static UserRegisterResponse from(User user) {
        return UserRegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

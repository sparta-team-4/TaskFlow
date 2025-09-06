package com.sparta.taskflow.domain.user.dto.response;

import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDetailsResponse(
        Long id,
        String username,
        String email,
        String name,
        Role role,
        LocalDateTime createdAt
) {
    public static UserDetailsResponse from(User user) {
        return UserDetailsResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

package com.sparta.taskflow.domain.comment.dto;

import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final Role role;

    @Builder
    private UserDto(Long id, String username,
                   String name, String email,
                   Role role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static UserDto create(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}

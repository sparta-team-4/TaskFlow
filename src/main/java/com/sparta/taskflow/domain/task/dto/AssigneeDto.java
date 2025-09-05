package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AssigneeDto {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;

    @Builder
    private AssigneeDto(Long id, String username,
                       String name, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public static AssigneeDto create(User assignee) {
        return AssigneeDto.builder()
                .id(assignee.getId())
                .username(assignee.getUsername())
                .name(assignee.getName())
                .email(assignee.getEmail())
                .build();
    }
}

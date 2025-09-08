package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class Dto {

    @Getter
    public static class AssigneeDto {
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

    @Getter
    public static class AssigneeListDto {
        private final List<Dto.AssigneeDto> user;

        @Builder
        private AssigneeListDto(List<Dto.AssigneeDto> user) {
            this.user = user;
        }

        public static AssigneeListDto create(List<Dto.AssigneeDto> teamMember) {
            return AssigneeListDto.builder()
                    .user(teamMember)
                    .build();
        }
    }
}

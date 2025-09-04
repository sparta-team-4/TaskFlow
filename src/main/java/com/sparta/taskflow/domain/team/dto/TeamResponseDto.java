package com.sparta.taskflow.domain.team.dto;

import com.sparta.taskflow.domain.team.entity.Team;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TeamResponseDto {

    @Getter
    public static class Create {
        private final Long id;
        private final String name;
        private final String description;
        private final LocalDateTime createdAt;
        private final List<Object> members;

        private Create(Team team) {
            this.id = team.getId();
            this.name = team.getName();
            this.description = team.getDescription();
            this.createdAt = team.getCreatedAt();
            this.members = Collections.emptyList();
        }

        public static Create from(Team team) {
            return new Create(team);
        }
    }
}
package com.sparta.taskflow.domain.team.dto;

import com.sparta.taskflow.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String name;
        private String description;

        public Team toEntity() {
            return Team.builder()
                    .name(this.name)
                    .description(this.description)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String name;
        private String description;
    }
}
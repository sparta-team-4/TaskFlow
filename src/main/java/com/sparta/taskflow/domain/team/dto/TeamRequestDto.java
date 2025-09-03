package com.sparta.taskflow.domain.team.dto;

import com.sparta.taskflow.domain.team.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamRequestDto {

    @Getter
    @NoArgsConstructor
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
}

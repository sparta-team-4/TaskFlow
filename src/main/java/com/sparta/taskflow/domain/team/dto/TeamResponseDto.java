package com.sparta.taskflow.domain.team.dto;

import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeamResponseDto {

    // 팀 생성에 대한 응답 DTO
    @Getter
    public static class Create {
        private final Long id;
        private final String name;
        private final String description;
        private final LocalDateTime createdAt;
        private final List<Object> members; // 생성 시에는 항상 빈 리스트

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

    /**
     * 팀 상세 정보 조회를 위한 응답 DTO
     * 팀의 기본 정보와 함께 소속된 멤버 목록을 포함
     */
    @Getter
    public static class Get {
        private final Long id;
        private final String name;
        private final String description;
        private final LocalDateTime createdAt;
        private final List<MemberInfo> members;

        private Get(Team team) {
            this.id = team.getId();
            this.name = team.getName();
            this.description = team.getDescription();
            this.createdAt = team.getCreatedAt();
            this.members = team.getTeamMembers().stream()
                    .map(teamMember -> MemberInfo.from(teamMember.getUser()))
                    .collect(Collectors.toList());
        }

        public static Get from(Team team) {
            return new Get(team);
        }

        // 팀 멤버 한 명의 정보를 담는 중첩 DTO
        @Getter
        public static class MemberInfo {
            private final Long id;
            private final String username;
            private final String name;
            private final String email;
            private final Role role;
            private final LocalDateTime createdAt;

            private MemberInfo(User user) {
                this.id = user.getId();
                this.username = user.getUsername();
                this.name = user.getName();
                this.email = user.getEmail();
                this.role = user.getRole();
                this.createdAt = user.getCreatedAt();
            }

            public static MemberInfo from(User user) {
                return new MemberInfo(user);
            }
        }
    }

    @Getter
    public static class Search {
        private final Long id;
        private final String name;
        private final String description;

        private Search(Team team) {
            this.id = team.getId();
            this.name = team.getName();
            this.description = team.getDescription();
        }

        public static Search from(Team team) {
            return new Search(team);
        }
    }
}
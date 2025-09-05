package com.sparta.taskflow.domain.user.dto;

import com.sparta.taskflow.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    @Getter
    public static class Simple {
        private final Long id;
        private final String username;
        private final String name;
        private final String email;

        private Simple(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.email = user.getEmail();
        }

        public static Simple from(User user) {
            return new Simple(user);
        }
    }
}
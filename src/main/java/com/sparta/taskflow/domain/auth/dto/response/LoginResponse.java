package com.sparta.taskflow.domain.auth.dto.response;

public record LoginResponse(String token) {

    public static LoginResponse of(String token) {
        return new LoginResponse(token);
    }
}

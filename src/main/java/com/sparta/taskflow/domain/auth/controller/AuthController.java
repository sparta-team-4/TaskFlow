package com.sparta.taskflow.domain.auth.controller;

import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.auth.dto.request.LoginRequest;
import com.sparta.taskflow.domain.auth.dto.request.UserRegisterRequest;
import com.sparta.taskflow.domain.auth.dto.response.LoginResponse;
import com.sparta.taskflow.domain.auth.dto.response.UserRegisterResponse;
import com.sparta.taskflow.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> userRegister(@Valid @RequestBody UserRegisterRequest request) {
        return ApiResponse.created(
                authService.userRegister(request),
                "회원가입이 완료되었습니다."
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(
                authService.login(request),
                "로그인이 완료되었습니다."
        );
    }
}

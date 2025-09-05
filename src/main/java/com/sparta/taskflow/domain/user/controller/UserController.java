package com.sparta.taskflow.domain.user.controller;

import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.user.dto.response.UserDetailsResponse;
import com.sparta.taskflow.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getLoginUserInfo(@AuthenticationPrincipal Long userId) {
        return ApiResponse.success(
                userQueryService.getUserDetails(userId),
                "사용자 정보를 조회했습니다."
        );
    }
}

package com.sparta.taskflow.domain.user.controller;

import com.sparta.taskflow.common.response.ApiResponse;

import com.sparta.taskflow.domain.user.dto.UserResponseDto;
import com.sparta.taskflow.domain.user.service.external.UserQueryService;
import com.sparta.taskflow.domain.user.dto.response.UserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<UserResponseDto.Simple>>> getAvailableUsers(
            @RequestParam Long teamId) {

        List<UserResponseDto.Simple> responseDto = userQueryService.getAvailableUsersForTeam(teamId);
        return ApiResponse.success(responseDto, "추가 가능한 사용자 목록을 조회했습니다.");
    }

}

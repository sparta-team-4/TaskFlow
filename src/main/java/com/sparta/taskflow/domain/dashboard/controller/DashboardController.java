package com.sparta.taskflow.domain.dashboard.controller;

import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.dashboard.dto.response.TaskStatisticsResponse;
import com.sparta.taskflow.domain.dashboard.service.DashboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardQueryService dashboardQueryService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TaskStatisticsResponse>> getBoardStatistics(@AuthenticationPrincipal Long userId) {
        TaskStatisticsResponse taskStatisticsResponse = dashboardQueryService.getDashboardStatistics(userId);
        return ApiResponse.success(taskStatisticsResponse, "대시보드 통계 조회 완료");
    }
}

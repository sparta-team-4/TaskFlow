package com.sparta.taskflow.domain.activitylog.controller;

import com.sparta.taskflow.common.response.ApiPageResponse;
import com.sparta.taskflow.domain.activitylog.dto.reponse.ActivityLogResponse;
import com.sparta.taskflow.domain.activitylog.dto.reponse.MyActivityResponse;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.activitylog.service.internal.ActivityLogInternalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogInternalServiceImpl activityLogInternalServiceImpl;

    @GetMapping
    public ResponseEntity<ApiPageResponse<ActivityLogResponse>> getActivityLogs(@RequestParam(required = false) ActivityType type,
                                                                                @RequestParam(required = false) Long taskId,
                                                                                @RequestParam(required = false) LocalDate startDate,
                                                                                @RequestParam(required = false) LocalDate endDate,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ActivityLogResponse> activityLogResponsePage = activityLogInternalServiceImpl.getActivityLogs(type, taskId, startDate, endDate, pageable)
                .map(ActivityLogResponse::from);
        return ApiPageResponse.success(
                activityLogResponsePage,
                "활동 로그를 조회했습니다."
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ApiPageResponse<MyActivityResponse>> getRecentMyActivityLogs(@AuthenticationPrincipal Long loginUserId,
                                                                                       @RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MyActivityResponse> response = activityLogInternalServiceImpl.getMyActivityLogs(loginUserId, pageable);
        return ApiPageResponse.success(response, "활동 내역 조회 완료");
    }
}

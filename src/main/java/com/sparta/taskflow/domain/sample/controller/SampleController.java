package com.sparta.taskflow.domain.sample.controller;

import com.sparta.taskflow.common.response.ApiPageResponse;
import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.sample.dto.response.SampleResponse;
import com.sparta.taskflow.domain.sample.exception.SampleErrorCode;
import com.sparta.taskflow.domain.sample.exception.SampleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/samples")
@RequiredArgsConstructor
public class SampleController {

    @GetMapping("/page")
    public ResponseEntity<ApiPageResponse<SampleResponse>> getTestPage() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        PageImpl<SampleResponse> userResponses = new PageImpl<>(
                List.of(new SampleResponse("message", "data"), new SampleResponse("message", "data"), new SampleResponse("message", "data")),
                pageRequest,
                pageRequest.getPageNumber()
        );
        return ApiPageResponse.success(userResponses, "성공!!");
    }

    @GetMapping("/api")
    public ResponseEntity<ApiResponse<SampleResponse>> getApi() {
        return ApiResponse.success(new SampleResponse("message", "data"), "성공!!");
    }

    @GetMapping("/api-empty")
    public ResponseEntity<ApiResponse<SampleResponse>> getApiEmpty() {
        return ApiResponse.success(null, "성공!!");
    }

    @GetMapping("/error")
    public ResponseEntity<ApiResponse<SampleResponse>> getError() {
        throw new SampleNotFoundException(SampleErrorCode.SAMPLE_NOT_FOUND);
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<Long>> getUser(@AuthenticationPrincipal Long userId) {
        return ApiResponse.success(userId, "토큰 안에 있는 id");
    }
}

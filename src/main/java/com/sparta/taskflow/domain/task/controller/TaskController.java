package com.sparta.taskflow.domain.task.controller;

import com.sparta.taskflow.common.response.ApiPageResponse;
import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.task.dto.*;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    //Task 목록 조회
    @GetMapping("/api/tasks")
    public ResponseEntity<ApiPageResponse<TaskResponse>> getByCategory(@RequestParam(required = false) Long assigneeId,
                                                                       @RequestParam(required = false) String keyword,
                                                                       @RequestParam(required = false) TaskStatus status,
                                                                       @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TaskResponse> taskResponse = taskService.getByCategory(status, keyword, assigneeId, pageable);
        return ApiPageResponse.success(taskResponse, "Task 목록을 조회했습니다.");
    }
    //Task 상세 조회
    @GetMapping("/api/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getById(@PathVariable Long taskId){
        return ApiResponse.success(taskService.getById(taskId), "Task를 조회했습니다.");
    }

    //Task 생성
    @PostMapping("/api/tasks")
    public ResponseEntity<ApiResponse<TaskResponse>> create(@Valid @RequestBody TaskRequest taskRequest,
                                                            @AuthenticationPrincipal Long loginUserId){
        return ApiResponse.created(taskService.create(taskRequest, loginUserId), "Task가 생성되었습니다.");
    }

    //Task 수정
    @PatchMapping("/api/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> update(@PathVariable Long taskId,
                                                            @Valid @RequestBody UpdateRequest updateRequest){
        return ApiResponse.success(taskService.update(taskId, updateRequest), "Task가 수정되었습니다.");
    }

    //Task 상태 수정
    @PatchMapping("/api/tasks/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateStatus(@PathVariable Long taskId,
                                                                  @Valid @RequestBody StatusRequest statusRequest){
        return ApiResponse.success(taskService.updateStatus(taskId, statusRequest), "작업 상태가 업데이트되었습니다.");
    }

    //Task 삭제
    @DeleteMapping("/api/tasks/{taskId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long taskId){
        taskService.delete(taskId);
        return ApiResponse.success(null, "Task가 삭제되었습니다.");
    }

}

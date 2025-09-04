package com.sparta.taskflow.domain.task.controller;

import com.sparta.taskflow.domain.task.dto.*;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    //인증&인가 필요?

    //Task 목록 조회
    @GetMapping("/api/tasks")
    public ResponseEntity<CategoryResponse> getByCategory(@RequestParam Long assigneeId,
                                                            @RequestParam String keyword,
                                                            @RequestParam TaskStatus status,
                                                            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        CategoryResponse categoryResponse = taskService.getByCategory(status, keyword, assigneeId, pageable);
        return ResponseEntity.ok(categoryResponse);
    }
    //Task 상세 조회
    @GetMapping("/api/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getByCategory(@PathVariable Long taskId){
        return ResponseEntity.ok(taskService.getById(taskId));
    }

    //Task 생성
    @PostMapping("/api/tasks")
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest taskRequest){
        return ResponseEntity.ok(taskService.create(taskRequest));
    }

    //Task 수정
    @PatchMapping("/api/tasks/{taskId}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long taskId, @RequestBody UpdateRequest updateRequest){
        return ResponseEntity.ok(taskService.update(taskId, updateRequest));
    }

    //Task 상태 수정
    @PatchMapping("/api/tasks/{taskId}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long taskId, @RequestBody StatusRequest statusRequest){
        return ResponseEntity.ok(taskService.updateStatus(taskId, statusRequest));
    }

    //Task 삭제
    @DeleteMapping("/api/tasks/{taskId}")
    public void delete(@PathVariable Long taskId){
        taskService.delete(taskId);
    }

}

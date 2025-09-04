package com.sparta.taskflow.domain.task.service;

import com.sparta.taskflow.domain.task.dto.*;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserInternalService userInternalService;
    private final TaskRepository taskRepository;

    //Task 생성
    @Transactional
    public TaskResponse create(TaskRequest taskRequest) {
        User assignee = userInternalService.getByIdOrThrow(taskRequest.getAssigneeId());
        Task data = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .dueDate(taskRequest.getDueDate())
                .priority(taskRequest.getPriority())
                .user(assignee)
                .build();
        Task task = taskRepository.save(data);
        return TaskResponse.response(task);
    }

    //Task 목록 조회
    @Transactional(readOnly = true)
    public CategoryResponse getByCategory(TaskStatus status, String keyword, Long assigneeId, Pageable pageable) {
        Page<Task> taskPage = taskRepository.search(status, keyword, assigneeId, pageable);
        return CategoryResponse.getByCategoryResponse(DataDto.data(taskPage));
    }

    //Task 상세 조회
    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        Task task = taskRepository.findTaskByIdOrThrow(id);
        return TaskResponse.getByIdResponse(task);
    }

    //Task 수정
    @Transactional
    public TaskResponse update(Long id, UpdateRequest updateRequest) {
        Task task = taskRepository.findTaskByIdOrThrow(id);
        User assignee = userInternalService.getByIdOrThrow(updateRequest.getAssigneeId());
        task.update(updateRequest.getTitle(),
                updateRequest.getDescription(),
                updateRequest.getPriority(),
                updateRequest.getDueDate(),
                updateRequest.getStatus(),
                assignee);
        return TaskResponse.updateResponse(task);
    }

    //Task 상태 수정
    @Transactional
    public TaskResponse updateStatus(Long id, StatusRequest statusRequest) {
        Task task = taskRepository.findTaskByIdOrThrow(id);
        task.updateStatus(statusRequest.getStatus());
        return TaskResponse.updateStatusResponse(task);
    }

    //Task 삭제
    @Transactional
    public TaskResponse delete(Long id) {
        taskRepository.findTaskByIdOrThrow(id);
        taskRepository.setTrueTaskIsDeleted(id);
        return TaskResponse.deleteResponse(null);
    }
}

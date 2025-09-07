package com.sparta.taskflow.domain.task.service;

import com.sparta.taskflow.common.logs.annotation.Loggable;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.activitylog.service.internal.ActivityLogInternalServiceImpl;
import com.sparta.taskflow.domain.task.dto.*;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.internal.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserInternalService userInternalService;
    private final TaskRepository taskRepository;
    private final ActivityLogInternalServiceImpl activityLogInternalServiceImpl;

    //Task 생성
    @Loggable
    @Transactional
    public TaskResponse create(TaskRequest taskRequest, Long loginUserId) {
        User assignee = userInternalService.getByIdOrThrow(taskRequest.getAssigneeId());

        Task data = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .dueDate(taskRequest.getDueDate())
                .priority(taskRequest.getPriority())
                .assignee(assignee)
                .endDate(null)
                .owner(loginUserId)
                .build();

        Task task = taskRepository.save(data);

        activityLogInternalServiceImpl.log(ActivityType.TASK_CREATED, loginUserId, task.getId(), task.getTitle());

        return TaskResponse.create(task);
    }

    //Task 목록 조회
    @Transactional(readOnly = true)
    public Page<TaskResponse> getByCategory(TaskStatus status, String keyword, Long assigneeId, Pageable pageable) {
        Page<Task> taskPage = taskRepository.search(status, keyword, assigneeId, pageable);
        return taskPage.map(TaskResponse::create);
    }

    //Task 상세 조회
    @Transactional(readOnly = true)
    public TaskResponse getById(Long taskId) {
        Task task = taskRepository.findTaskByIdOrThrow(taskId);
        task.validateTaskNotDeleted();
        return TaskResponse.create(task);
    }

    //Task 수정
    @Loggable
    @Transactional
    public TaskResponse update(Long taskId, UpdateRequest updateRequest, Long loginUserId) {
        Task task = taskRepository.findTaskByIdOrThrow(taskId);
        task.validateTaskNotDeleted();
        User assignee = userInternalService.getByIdOrThrow(updateRequest.getAssigneeId());
        task.recordEndDate(updateRequest.getStatus());
        task.update(updateRequest.getTitle(),
                updateRequest.getDescription(),
                updateRequest.getPriority(),
                updateRequest.getDueDate(),
                updateRequest.getStatus(),
                assignee);

        activityLogInternalServiceImpl.log(ActivityType.TASK_UPDATED, loginUserId, taskId);

        return TaskResponse.create(task);
    }

    //Task 상태 수정
    @Loggable
    @Transactional
    public TaskResponse updateStatus(Long taskId, StatusRequest statusRequest, Long loginUserId) {
        Task task = taskRepository.findTaskByIdOrThrow(taskId);
        task.validateTaskNotDeleted();
        task.recordEndDate(statusRequest.getStatus());
        task.updateStatus(statusRequest.getStatus());

        TaskStatus ordStatus = task.getStatus();
        TaskStatus newStatus = statusRequest.getStatus();

        task.updateStatus(newStatus);

        activityLogInternalServiceImpl.log(ActivityType.TASK_STATUS_CHANGED, loginUserId, taskId, ordStatus, newStatus);

        return TaskResponse.create(task);
    }

    //Task 삭제
    @Loggable
    @Transactional
    public void delete(Long taskId, Long loginUserId) {
        Task task = taskRepository.findTaskByIdOrThrow(taskId);
        task.validateTaskNotDeleted();
        taskRepository.setTrueTaskIsDeleted(taskId);

        activityLogInternalServiceImpl.log(ActivityType.TASK_DELETED, loginUserId, taskId);
    }
}

package com.sparta.taskflow.domain.task.service;

import com.sparta.taskflow.domain.task.dto.TaskResponse;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import com.sparta.taskflow.domain.task.repository.dto.TeamProgressProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskInternalServiceImpl implements TaskInternalService {

    private final TaskRepository taskRepository;

    //internalService 메서드 구현
    //task id로 task 조회
    public Task getByIdOrThrow(Long taskId) {
        Task findTask = taskRepository.findTaskByIdOrThrow(taskId);
        findTask.validateTaskNotDeleted();
        return findTask;
    }

    //유저 id로 task 조회
    public List<Task> getAllById(Long assigneeId) {
        return taskRepository.findAllByAssigneeId(assigneeId);
    }

    @Override
    public List<TaskResponse> searchTasksByQuery(String query) {
        return taskRepository.findByTitleContainingIgnoreCase(query).stream()
                .map(TaskResponse::create)
                .toList();
    }

    @Override
    public List<TeamProgressProjection> findTeamProgress() { // ❗️추가
        return taskRepository.findTeamProgress();
    }
}

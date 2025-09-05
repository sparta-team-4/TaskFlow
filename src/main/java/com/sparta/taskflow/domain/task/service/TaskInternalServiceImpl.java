package com.sparta.taskflow.domain.task.service;

import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskInternalServiceImpl implements TaskInternalService {
    private final TaskRepository taskRepository;

    //internalService 메서드 구현
    //task id로 task 조회
    public Task getByIdOrThrow(Long taskId){
        Task findTask = taskRepository.findTaskByIdOrThrow(taskId);
        findTask.validateTaskNotDeleted();
        return findTask;
    }

    //유저 id로 task 조회
    public List<Task> getAllById(Long assigneeId){
        return taskRepository.findAllByAssigneeId(assigneeId);
    }
}

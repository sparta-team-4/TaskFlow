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
    public Task getByIdOrThrow(Long id){
        Task findTask = taskRepository.findTaskByIdOrThrow(id);
        findTask.validateTaskNotDeleted();
        return findTask;
    }

    public List<Task> getAllById(Long id){
        return taskRepository.findAllByAssigneeId(id);
    }
}

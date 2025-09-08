package com.sparta.taskflow.domain.task.service;

import com.sparta.taskflow.domain.task.dto.TaskResponse;
import com.sparta.taskflow.domain.task.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

public interface TaskInternalService {

    Task getByIdOrThrow(Long id);

    List<Task> getAllById(Long id);

    List<TaskResponse> searchTasksByQuery(String query);
}

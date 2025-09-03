package com.sparta.taskflow.domain.task.service;

import com.sparta.taskflow.domain.task.entity.Task;

import java.util.List;

public interface TaskInternalService {

    Task getByIdOrThrow(Long id);

    List<Task> getAllById(Long id);

    List<Task> getAllByIds(List<Long> ids);
}

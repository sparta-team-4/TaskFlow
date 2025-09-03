package com.sparta.taskflow.task.service;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskInternalService {
    Task getByIdOrThrow(Long id);
    List<Task> getAllById(Long id);
    List<Task> getAllByIds(List<Long> ids);
}

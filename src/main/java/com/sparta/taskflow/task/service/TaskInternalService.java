package com.sparta.taskflow.task.service;

import java.time.LocalDateTime;

public interface TaskInternalService {
    //Task 생성
    Task create (String title, String description, LocalDateTime dueDate, String priority, Long assigneeId);

    //Task 목록 조회
    Page<Task> getTasks(String status, int page, int size, String search, Long assigneeId);

    //Task 상세 조회
    Task getById(Long id);

    //Task 수정
    Task update (String title, String description, LocalDateTime dueDate, String priority, String status, Long assigneeId);

    //Task 상태 수정
    Task updateStatus(String status);

    //Task 삭제
    void delete (Long id);
}

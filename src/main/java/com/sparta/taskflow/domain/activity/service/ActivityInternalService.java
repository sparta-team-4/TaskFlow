package com.sparta.taskflow.domain.activity.service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityLogInternalService {

    //ID로 단일 활동로그 조회
    Activity getByIdOrThrow(Long id);

    //ID 리스트로 복수 활동로그 조회
    List<Activity> getAllByIds(List<Long> ids);

    //특정 User 활동로그 조회
    List<Activity> getActivitiesByUserId(Long userId);

    //특정 Task 활동로그 조회
    List<Activity> getActivitiesByTaskId(Long taskId);

}

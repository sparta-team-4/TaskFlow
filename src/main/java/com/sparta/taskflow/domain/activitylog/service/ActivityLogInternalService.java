package com.sparta.taskflow.domain.activitylog.service;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;

import java.util.List;

public interface ActivityLogInternalService {

    //ID로 단일 활동로그 조회
    ActivityLog getByIdOrThrow(Long id);

    //ID 리스트로 복수 활동로그 조회
    List<ActivityLog> getAllByIds(List<Long> ids);

    //특정 User 활동로그 조회
    List<ActivityLog> getActivitiesByUserId(Long userId);

    //특정 Task 활동로그 조회
    List<ActivityLog> getActivitiesByTaskId(Long taskId);

}

package com.sparta.taskflow.domain.activitylog.service.internal;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;

public interface ActivityLogInternalService {

    //ID로 단일 활동로그 조회
    ActivityLog getByIdOrThrow(Long id);

}

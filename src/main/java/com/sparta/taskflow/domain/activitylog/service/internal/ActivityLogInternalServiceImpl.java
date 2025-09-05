package com.sparta.taskflow.domain.activitylog.service.internal;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.activitylog.exception.ActivityLogErrorCode;
import com.sparta.taskflow.domain.activitylog.exception.ActivityLogUnauthorizedException;
import com.sparta.taskflow.domain.activitylog.repository.ActivityLogRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.UserInternalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityLogInternalServiceImpl implements  ActivityLogInternalService {

    private final ActivityLogRepository activityLogRepository;
    private final UserInternalServiceImpl userInternalServiceImpl;

    @Override
    @Transactional(readOnly = true)
    public ActivityLog getByIdOrThrow(Long id) {
        return activityLogRepository.findById(id)
                .orElseThrow(() -> new ActivityLogUnauthorizedException(ActivityLogErrorCode.ACTIVITY_LOG_UNAUTHORIZED));
    }

    @Transactional
    public void log(ActivityType type, Long userId, Long taskId, Object... args) {
        User user = userInternalServiceImpl.getByIdOrThrow(userId);

        ActivityLog activityLog = ActivityLog.create(type, user, taskId, args);

        activityLogRepository.save(activityLog);
    }
}


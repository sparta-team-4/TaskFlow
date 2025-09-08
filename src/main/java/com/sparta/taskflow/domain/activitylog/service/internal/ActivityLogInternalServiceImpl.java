package com.sparta.taskflow.domain.activitylog.service.internal;

import com.sparta.taskflow.domain.activitylog.dto.reponse.MyActivityResponse;
import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.activitylog.exception.ActivityLogErrorCode;
import com.sparta.taskflow.domain.activitylog.exception.ActivityLogUnauthorizedException;
import com.sparta.taskflow.domain.activitylog.repository.ActivityLogRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.internal.UserInternalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityLogInternalServiceImpl implements ActivityLogInternalService {

    private final ActivityLogRepository activityLogRepository;
    private final UserInternalServiceImpl userInternalServiceImpl;

    @Override
    public ActivityLog getByIdOrThrow(Long id) {
        return activityLogRepository.findById(id)
                .orElseThrow(() -> new ActivityLogUnauthorizedException(ActivityLogErrorCode.ACTIVITY_LOG_UNAUTHORIZED));
    }

    public Page<ActivityLog> getActivityLogs(ActivityType type,
                                             Long taskId,
                                             LocalDateTime startDate,
                                             LocalDateTime endDate,
                                             Pageable pageable) {
        return activityLogRepository.search(type, taskId, startDate, endDate, pageable);
    }

    @Transactional
    public void log(ActivityType type, Long userId, Long taskId, Object... args) {
        User user = userInternalServiceImpl.getByIdOrThrow(userId);

        ActivityLog activityLog = ActivityLog.create(type, user, taskId, args);

        activityLogRepository.save(activityLog);
    }

    public Page<MyActivityResponse> getMyActivityLogs(Long loginUserId, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<ActivityLog> myActivities = activityLogRepository.findAllByUserId(loginUserId, sortedPageable);

        return myActivities.map(MyActivityResponse::from);
    }
}


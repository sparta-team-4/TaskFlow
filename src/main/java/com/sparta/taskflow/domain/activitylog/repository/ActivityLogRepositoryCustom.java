package com.sparta.taskflow.domain.activitylog.repository;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ActivityLogRepositoryCustom {

    Page<ActivityLog> search(ActivityType type, Long taskId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

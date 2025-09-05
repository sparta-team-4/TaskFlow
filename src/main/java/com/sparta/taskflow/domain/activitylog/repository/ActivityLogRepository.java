package com.sparta.taskflow.domain.activitylog.repository;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}

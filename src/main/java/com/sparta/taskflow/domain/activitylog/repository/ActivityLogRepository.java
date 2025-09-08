package com.sparta.taskflow.domain.activitylog.repository;

import com.sparta.taskflow.domain.activitylog.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>, ActivityLogRepositoryCustom {

    @EntityGraph(attributePaths = {"user"})
    Page<ActivityLog> findAllByUserId(Long loginUserId, Pageable pageable);
}

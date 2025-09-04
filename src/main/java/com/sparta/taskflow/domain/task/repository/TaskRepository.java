package com.sparta.taskflow.domain.task.repository;

import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    default Task findTaskByIdOrThrow(Long postId) {
        return findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Task not found.")
        );
    }

    @Query("""
                SELECT t
                FROM Task t
                JOIN t.user u
                WHERE
                    (t.title LIKE %:keyword% OR t.description LIKE %:keyword%)
                AND
                    (t.status = :status)
                AND
                    (u.id = :assigneeId)
            """)
    Page<Task> search(
            @Param("status") TaskStatus status,
            @Param("keyword") String keyword,
            @Param("assigneeId") Long assigneeId,
            Pageable pageable
    );

    List<Task> findTaskByUserId(Long assigneeId);

    @Modifying
    @Query("UPDATE Task u SET u.isDeleted = true WHERE u.id = :id")
    void setTrueTaskIsDeleted(Long id);
}

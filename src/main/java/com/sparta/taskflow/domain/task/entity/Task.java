package com.sparta.taskflow.domain.task.entity;

import com.sparta.taskflow.common.entity.BaseEntity;
import com.sparta.taskflow.domain.task.enums.TaskPriority;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
import com.sparta.taskflow.domain.task.exception.CustomException;
import com.sparta.taskflow.domain.task.exception.TaskErrorCode;
import com.sparta.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 600)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User assignee;

    private Long owner;

    @Builder
    public Task(String title, String description,
                TaskPriority priority, LocalDateTime dueDate,
                User assignee, LocalDateTime endDate, Long owner) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.TODO;
        this.assignee = assignee;
        this.endDate = endDate;
        this.owner = owner;
    }

    public void update(String title, String description,
                       TaskPriority priority, LocalDateTime dueDate,
                       TaskStatus status, User assignee){
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
        this.assignee = assignee;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

    public void updateEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    //Task 삭제 검증
    public void validateTaskNotDeleted() {
        if (isDeleted()) {
            throw new CustomException(TaskErrorCode.DELETED_TASK);
        }
    }

    // Task 작성자인지 검증
    public void validateOwner(Long userId) {
        if (!owner.equals(userId)) {
            throw new CustomException(TaskErrorCode.OWNER_MISMATCH);
        }
    }

    //endDate 기록
    public void recordEndDate(TaskStatus status) {
        this.status = status;
        if (status == TaskStatus.DONE) {
            this.endDate = LocalDateTime.now();
        } else {
            this.endDate = null;
        }
    }

}

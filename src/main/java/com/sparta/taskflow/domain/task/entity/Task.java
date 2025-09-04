package com.sparta.taskflow.domain.task.entity;

import com.sparta.taskflow.common.entity.BaseEntity;
import com.sparta.taskflow.domain.task.enums.TaskPriority;
import com.sparta.taskflow.domain.task.enums.TaskStatus;
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

    @Column(length = 600)
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

    private LocalDateTime endDate; //nullable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Task(String title, String description, TaskPriority priority, LocalDateTime dueDate, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.TODO;
        this.user = user;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(String title, String description, TaskPriority priority, LocalDateTime dueDate, TaskStatus status, User user){
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
        this.user = user;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

    //Task 삭제 검증
    public void validateTaskNotDeleted() {
        if (isDeleted()) {
            throw new IllegalArgumentException();
        }
    }

    // 댓글 작성자인지 검증
    public void validateOwner(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new IllegalArgumentException();
        }
    }
}

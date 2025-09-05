package com.sparta.taskflow.domain.activitylog.entity;

import com.sparta.taskflow.common.entity.CreatedAtEntity;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityLog extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long taskId;

    @Builder
    public ActivityLog(ActivityType activityType, String description, User user, Long taskId) {
        this.type = activityType;
        this.description = description;
        this.user = user;
        this.taskId = taskId;
    }

    public static ActivityLog create(ActivityType type, String description, User user, Long taskId) {
        return ActivityLog.builder()
                .activityType(type)
                .description(description)
                .user(user)
                .taskId(taskId)
                .build();
    }
}

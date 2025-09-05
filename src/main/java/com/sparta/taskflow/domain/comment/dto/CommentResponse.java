package com.sparta.taskflow.domain.comment.dto;

import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private Long taskId;
    private Long userId;
    private User user;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private CommentResponse(Long id, String content,
                            Long taskId, Long userId,
                            User user, LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.taskId = taskId;
        this.userId = userId;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponse create(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(comment.getUserId())
                .user(comment.getTask().getUser())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}

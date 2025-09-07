package com.sparta.taskflow.domain.comment.entity;

import com.sparta.taskflow.common.entity.BaseEntity;
import com.sparta.taskflow.domain.comment.exception.CommentErrorCode;
import com.sparta.taskflow.domain.comment.exception.CommentException;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    @Builder
    public Comment (User user, String content, Task task, Comment parentComment) {
        this.user = user;
        this.content = content;
        this.task = task;
        this.parentComment = parentComment;
    }

    public void update(String content) {
        this.content = content;
    }

    //Comment 작성자인지 검증
    public void validateOwner(Long loginUserId) {
        if (!(user.getId()).equals(loginUserId)) {
            throw new CommentException(CommentErrorCode.OWNER_MISMATCH);
        }
    }
}

package com.sparta.taskflow.domain.comment.entity;

import com.sparta.taskflow.common.entity.BaseEntity;
import com.sparta.taskflow.domain.comment.exception.CommentErrorCode;
import com.sparta.taskflow.domain.comment.exception.CustomException;
import com.sparta.taskflow.domain.task.entity.Task;
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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private Long userId; //댓글 작성자 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    @Builder
    public Comment (String name, String content, Long userId, Task task, Comment parentComment) {
        this.name = name;
        this.content = content;
        this.userId = userId;
        this.task = task;
        this.parentComment = parentComment;
    }

    public void update(String content) {
        this.content = content;
    }

    //Comment 작성자인지 검증
    public void validateOwner(Long loginUserId) {
        if (!userId.equals(loginUserId)) {
            throw new CustomException(CommentErrorCode.OWNER_MISMATCH);
        }
    }
}

package com.sparta.taskflow.domain.comment.repository;

import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.exception.CommentErrorCode;
import com.sparta.taskflow.domain.comment.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    default Comment findByIdOrThrow(long commentId) {
        return findById(commentId).orElseThrow(
                () -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND)
        );
    }
}

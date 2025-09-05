package com.sparta.taskflow.domain.comment.repository;

import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.exception.CommentErrorCode;
import com.sparta.taskflow.domain.comment.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    default Comment findByIdOrThrow(long commentId) {
        return findById(commentId).orElseThrow(
                () -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND)
        );
    }
    @EntityGraph(attributePaths = {"childComments"})
    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);

    List<Comment> findAllByUser_Id(Long userId);
}

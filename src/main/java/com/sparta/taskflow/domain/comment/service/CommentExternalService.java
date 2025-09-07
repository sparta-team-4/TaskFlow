package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.common.logs.annotation.Loggable;
import com.sparta.taskflow.domain.activitylog.enums.ActivityType;
import com.sparta.taskflow.domain.activitylog.service.internal.ActivityLogInternalServiceImpl;
import com.sparta.taskflow.domain.comment.dto.CommentRequest;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import com.sparta.taskflow.domain.comment.dto.UpdateRequest;
import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.service.TaskInternalService;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.internal.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentExternalService {
    private final CommentRepository commentRepository;
    private final UserInternalService userInternalService;
    private final TaskInternalService taskInternalService;
    private final ActivityLogInternalServiceImpl activityLogInternalServiceImpl;

    //Task의 댓글 목록 조회
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentByPage(Long taskId, Pageable pageable){
        taskInternalService.getByIdOrThrow(taskId);
        Page<Comment> commentPage = commentRepository.findAllByTaskId(taskId, pageable);
        return commentPage.map(CommentResponse::create);
    }

    //댓글 생성
    @Loggable
    @Transactional
    public CommentResponse create(Long loginUserId, Long taskId, CommentRequest commentRequest){
        Task task = taskInternalService.getByIdOrThrow(taskId);
        User user = userInternalService.getByIdOrThrow(loginUserId);
        Comment parentComment = null;
        if (commentRequest.getParentId() != null) {
            parentComment = commentRepository.findByIdOrThrow(commentRequest.getParentId());
        }
        Comment comment = Comment.builder()
                .user(user)
                .content(commentRequest.getContent())
                .task(task)
                .parentComment(parentComment)
                .build();
        commentRepository.save(comment);

        activityLogInternalServiceImpl.log(ActivityType.COMMENT_CREATED, loginUserId, taskId);

        return CommentResponse.create(comment);
    }

    //댓글 수정
    @Loggable
    @Transactional
    public CommentResponse update(Long loginUserId, Long taskId, Long commentId, UpdateRequest updateRequest){
        taskInternalService.getByIdOrThrow(taskId);
        Comment comment = commentRepository.findByIdOrThrow(commentId);
        comment.validateOwner(loginUserId);
        comment.update(updateRequest.getContent());

        activityLogInternalServiceImpl.log(ActivityType.COMMENT_UPDATED, loginUserId, taskId);

        return CommentResponse.create(comment);
    }

    //댓글 삭제
    @Loggable
    @Transactional
    public void delete(Long loginUserId, Long taskId,Long commentId){
        taskInternalService.getByIdOrThrow(taskId);
        Comment comment =commentRepository.findByIdOrThrow(commentId);
        comment.validateOwner(loginUserId);
        commentRepository.delete(comment);

        activityLogInternalServiceImpl.log(ActivityType.COMMENT_DELETED, loginUserId, taskId);
    }
}

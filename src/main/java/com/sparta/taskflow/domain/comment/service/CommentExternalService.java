package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.dto.CommentRequest;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import com.sparta.taskflow.domain.comment.dto.UpdateRequest;
import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import com.sparta.taskflow.domain.task.entity.Task;
import com.sparta.taskflow.domain.task.service.TaskInternalService;
import com.sparta.taskflow.domain.user.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentExternalService {
    private final CommentRepository commentRepository;
    private final UserInternalService userInternalService;
    private final TaskInternalService taskInternalService;

    //Task의 댓글 목록 조회
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentByPage(){
        return null;
    }

    //댓글 생성
    @Transactional(readOnly = true)
    public CommentResponse create(Long taskId, CommentRequest commentRequest, Long loginUserId){
        Task task = taskInternalService.getByIdOrThrow(taskId);
        Comment comment = Comment.builder()
                .name(userInternalService.getByIdOrThrow(loginUserId).getName())
                .content(commentRequest.getContent())
                .task(task)
                .parentComment(commentRequest.getParentId())
                .build();
        return CommentResponse.create(comment);
    }

    //댓글 수정
    @Transactional(readOnly = true)
    public CommentResponse update(Long commentId, UpdateRequest updateRequest){
        Comment comment =commentRepository.findByIdOrThrow(commentId);
        comment.update(updateRequest.getContent());
        return null;
    }

    //댓글 삭제
    @Transactional(readOnly = true)
    public void delete(Long commentId){}
}

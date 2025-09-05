package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.dto.CommentRequest;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentExternalService {
    private final CommentRepository commentRepository;
    private final CommentInternalService commentInternalService;

    //Task의 댓글 목록 조회
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentByPage(){
        return null;
    }

    //댓글 생성
    @Transactional(readOnly = true)
    public CommentResponse create(Long taskId, CommentRequest commentRequest){


        return null;
    }

    //댓글 수정
    @Transactional(readOnly = true)
    public CommentResponse update(Long commentId){
        return null;
    }

    //댓글 삭제
    @Transactional(readOnly = true)
    public void delete(Long commentId){}
}

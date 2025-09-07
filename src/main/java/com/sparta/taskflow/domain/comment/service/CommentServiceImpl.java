package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.entity.Comment;
import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentInternalService{
    private final CommentRepository commentRepository;

    //comment id로 조회
    public Comment getByIdOrThrow(Long commentId){
        return commentRepository.findByIdOrThrow(commentId);
    }

    //userId의 comment 조회
    public List<Comment> getAllById(Long userId){
        return commentRepository.findAllByUser_Id(userId);
    }
}

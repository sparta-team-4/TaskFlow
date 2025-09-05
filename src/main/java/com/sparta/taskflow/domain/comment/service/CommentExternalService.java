package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentExternalService {
    private CommentRepository commentRepository;
    private CommentInternalService commentInternalService;
}

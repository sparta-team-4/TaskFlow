package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CommentInternalService {

    Comment getByIdOrThrow(Long id);

    List<Comment> getAllById(Long id);
}

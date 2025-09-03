package com.sparta.taskflow.domain.comment.service;

import com.sparta.taskflow.domain.comment.entity.Comment;

import java.util.List;

public interface CommentInternalService {

    Comment getByIdOrThrow(Long id);

    List<Comment> getAllById(Long id);

    List<Comment> getAllByIds(List<Long> ids);
}

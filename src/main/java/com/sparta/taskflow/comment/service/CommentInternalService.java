package com.sparta.taskflow.comment.service;

import java.util.List;

public interface CommentInternalService {
    Comment getByIdOrThrow(Long id);
    List<Comment> getAllById(Long id);
    List<Comment> getAllByIds(List<Long> ids);
}

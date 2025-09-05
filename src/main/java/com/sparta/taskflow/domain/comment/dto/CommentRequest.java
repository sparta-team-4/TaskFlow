package com.sparta.taskflow.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {
    private String content;
    private Long parentId;
}

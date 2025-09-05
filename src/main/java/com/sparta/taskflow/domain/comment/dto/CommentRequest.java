package com.sparta.taskflow.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {
    @NotBlank
    private String content;
    private Long parentId; //nullable
}

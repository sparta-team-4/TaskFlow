package com.sparta.taskflow.domain.comment.controller;

import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentController {

    //Task의 댓글 목록 조회
    @GetMapping("/api/tasks/{taskid}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> getAllComment(){
        return ApiResponse.success();
    }

    //댓글 & 대댓글 생성
    @GetMapping("/api/tasks/{taskid}/comments")
    public CommentResponse getAllComment(){
        return null;
    }

    //댓글 수정
    @GetMapping("/api/tasks/{taskId}/comments/{commentId}")
    public CommentResponse getAllComment(){
        return null;
    }

    //댓글 삭제
    @GetMapping("/api/tasks/{taskId}/comments/{commentId}")
    public CommentResponse getAllComment(){
        return null;
    }
}

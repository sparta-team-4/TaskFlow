package com.sparta.taskflow.domain.comment.controller;

import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.comment.dto.CommentRequest;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import com.sparta.taskflow.domain.comment.dto.UpdateRequest;
import com.sparta.taskflow.domain.comment.service.CommentExternalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentController {
    private CommentExternalService commentService;

    //Task의 댓글 목록 조회
    @GetMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> getAllComment(@AuthenticationPrincipal Long loginUserId){
        return ApiResponse.success("댓글이 생성되었습니다.");
    }

    //댓글 & 대댓글 생성
    @GetMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@AuthenticationPrincipal Long loginUserId,
                                                                      @PathVariable Long taskId,
                                                                      @RequestBody CommentRequest commentRequest){
        return ApiResponse.success(commentService.create(loginUserId,commentRequest,taskId),"댓글이 생성되었습니다.");
    }

    //댓글 수정
    @GetMapping("/api/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> update(@PathVariable Long taskId,
                                                               @PathVariable Long commentId,
                                                               @RequestBody UpdateRequest updateRequest){
        return ApiResponse.success(commentService.update(loginUserId,commentRequest,taskId),"댓글이 수정되었습니다.");
    }

    //댓글 삭제
    @GetMapping("/api/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> delete(@PathVariable Long taskId,
                                                               @PathVariable Long commentId){
        return ApiResponse.success(commentService.delete(taskId, commentId),"댓글이 삭제되었습니다.");
    }
}

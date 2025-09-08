package com.sparta.taskflow.domain.comment.controller;

import com.sparta.taskflow.common.response.ApiPageResponse;
import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.comment.dto.CommentRequest;
import com.sparta.taskflow.domain.comment.dto.CommentResponse;
import com.sparta.taskflow.domain.comment.dto.UpdateRequest;
import com.sparta.taskflow.domain.comment.enums.CommentSort;
import com.sparta.taskflow.domain.comment.service.CommentExternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class CommentController {
    private final CommentExternalService commentService;

    //Task의 댓글 목록 조회
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<ApiPageResponse<CommentResponse>> getAllComment(@PathVariable Long taskId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam CommentSort sort){
        Sort.Direction direction = sort == CommentSort.NEWEST
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<CommentResponse> commentResponse = commentService.getCommentByPage(taskId, pageable);
        return ApiPageResponse.success(commentResponse, "댓글 목록을 조회했습니다.");
    }

    //댓글 & 대댓글 생성
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@AuthenticationPrincipal Long loginUserId,
                                                                      @PathVariable Long taskId,
                                                                      @Valid @RequestBody CommentRequest commentRequest){
        return ApiResponse.success(commentService.create(loginUserId, taskId, commentRequest),"댓글이 생성되었습니다.");
    }

    //댓글 수정
    @PatchMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> update(@AuthenticationPrincipal Long loginUserId,
                                                               @PathVariable Long taskId,
                                                               @PathVariable Long commentId,
                                                               @Valid @RequestBody UpdateRequest updateRequest){
        return ApiResponse.success(commentService.update(loginUserId, taskId, commentId, updateRequest),"댓글이 수정되었습니다.");
    }

    //댓글 삭제
    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal Long loginUserId,
                                                    @PathVariable Long taskId,
                                                    @PathVariable Long commentId){
        commentService.delete(loginUserId, taskId, commentId);
        return ApiResponse.success(null,"댓글이 삭제되었습니다.");
    }
}

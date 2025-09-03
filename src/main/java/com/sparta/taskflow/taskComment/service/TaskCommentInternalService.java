package com.sparta.taskflow.taskComment.service;

public interface TaskCommentInternalService {
    //댓글 생성
    TaskComment create(String content, Long parentId);

    //Task의 댓글 목록 조회
    Page<Task> getComments(int page, int size, String search, String sort);

    //댓글 수정
    Task update(String content);

    //댓글 삭제
    void delete(Long id);
}

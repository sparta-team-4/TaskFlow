package com.sparta.taskflow.domain.user.service;

public interface UserInternalService {
    //ID로 User 조회
    //다중 유저 조회 기능은 제외
    User getByIdOrThrow(Long userId);
}

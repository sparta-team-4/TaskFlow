package com.sparta.taskflow.domain.user.service;

import com.sparta.taskflow.domain.user.entity.User;

public interface UserInternalService {
    //ID로 User 조회
    //다중 유저 조회 기능은 제외
    User getByIdOrThrow(Long userId);

    //프로필 수정 기능 남겨둠
    User updateName(Long id, String name);
    User changePassword(Long id, String currentPassword, String newPassword);
}

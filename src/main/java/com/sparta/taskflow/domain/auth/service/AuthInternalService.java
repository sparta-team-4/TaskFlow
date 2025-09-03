package com.sparta.taskflow.domain.auth.service;

import com.sparta.taskflow.domain.user.entity.User;

public interface AuthInternalService {

    //회원가입
    User register(String username, String name, String email, String password);

    //로그인
    String login(String username, String password);

    //로그아웃
    void logout(String token);

    //회원 탈퇴
    void withdraw(String password);

    //토큰 재발급
    TokenResponse refreshToken(String refreshToken);
}

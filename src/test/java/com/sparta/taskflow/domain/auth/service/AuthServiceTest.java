package com.sparta.taskflow.domain.auth.service;

import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.domain.auth.dto.request.UserRegisterRequest;
import com.sparta.taskflow.domain.auth.dto.response.UserRegisterResponse;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.exception.DuplicateUsernameException;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import com.sparta.taskflow.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("회원가입에 성공한다.")
    void userRegister_success() {
        // given
        UserRegisterRequest request = new UserRegisterRequest(
                "test",
                "won se",
                "test@test.com",
                "password1234!"
        );
        LocalDateTime createdAt = LocalDateTime.of(2025, 9, 3, 19, 48);
        User user = TestUtils.createEntity(User.class, Map.of(
                "id", 1L,
                "username", "test",
                "email", "test@test.com",
                "name", "won se",
                "role", Role.USER,
                "createdAt", createdAt
        ));

        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        UserRegisterResponse response = authService.userRegister(request);

        // then
        assertThat(response)
                .extracting("id", "username", "email", "name", "role", "createdAt")
                .contains(1L, "test", "test@test.com", "won se", Role.USER, createdAt);

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 사용자명이 있으면 회원가입을 할 수 없다.")
    void userRegister_fail_whenDuplicateUsername() {
        // given
        UserRegisterRequest request = new UserRegisterRequest(
                "test",
                "won se",
                "test@test.com",
                "password1234!"
        );

        // when
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        // then
        assertThatThrownBy(() -> authService.userRegister(request))
                .isInstanceOf(DuplicateUsernameException.class)
                .hasMessage("이미 존재하는 사용자명입니다");
    }
}
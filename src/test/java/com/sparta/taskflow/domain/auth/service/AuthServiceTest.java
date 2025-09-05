package com.sparta.taskflow.domain.auth.service;

import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.common.utils.JwtUtils;
import com.sparta.taskflow.domain.auth.dto.request.LoginRequest;
import com.sparta.taskflow.domain.auth.dto.request.UserRegisterRequest;
import com.sparta.taskflow.domain.auth.dto.response.LoginResponse;
import com.sparta.taskflow.domain.auth.dto.response.UserRegisterResponse;
import com.sparta.taskflow.domain.auth.exception.DuplicateUsernameException;
import com.sparta.taskflow.domain.auth.exception.InvalidUsernameOrPasswordException;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import com.sparta.taskflow.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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

    @Mock
    JwtUtils jwtUtils;

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

    @Nested
    @DisplayName("로그인")
    class LoginTest {

        @Test
        @DisplayName("로그인에 성공하면 token 을 발행한다.")
        void login_success() {
            // given
            String rawPassword = "password1234!";
            String encodedPassword = "fjdsaklfjdsaonewoaf";
            LoginRequest request = new LoginRequest("test", rawPassword);
            User user = TestUtils.createEntity(User.class, Map.of(
                    "id", 1L,
                    "username", "test",
                    "password", encodedPassword,
                    "role", Role.USER
            ));

            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1eW5CaWlyY25ad3NSLzFHOWxMYmw5Q1dWZ25BcHJCY0lqWDNzZG4yWHRjPSIsImV4cCI6MTc1Njk1NzcxMiwiaWF0IjoxNzU2OTU1OTEyfQ.Mf4CysmSxaQqTf0o8xpQXzyM-AK9o3L_3dj5V-Nrzqs";
            given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
            given(jwtUtils.createToken(anyLong(), any(Role.class))).willReturn(token);

            // when
            LoginResponse response = authService.login(request);

            // then
            assertThat(response.token()).isEqualTo(token);
        }

        @Test
        @DisplayName("유저 ID가 일치하지 않으면 로그인이 불가능하다.")
        void login_fail_whenInvalidUsername() {
            // given
            LoginRequest request = new LoginRequest("test", "password1234!");

            // when
            when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(InvalidUsernameOrPasswordException.class)
                    .hasMessage("잘못된 사용자명 또는 비밀번호입니다");
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 로그인이 불가능하다.")
        void login_fail_whenInvalidPassword() {
            // given
            String rawPassword = "password1234!";
            String encodedPassword = "fjdsaklfjdsaonewoaf";
            LoginRequest request = new LoginRequest("test", rawPassword);
            User user = TestUtils.createEntity(User.class, Map.of(
                    "id", 1L,
                    "username", "test",
                    "password", encodedPassword,
                    "role", Role.USER
            ));
            given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

            // when
            when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

            // then
            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(InvalidUsernameOrPasswordException.class)
                    .hasMessage("잘못된 사용자명 또는 비밀번호입니다");
        }
    }

    @Nested
    @DisplayName("회원 탈퇴")
    class WithdrawTest {

        @Test
        @DisplayName("비밀번호가 일치하면 회원탈퇴가 된다.")
        void withdraw_success() {
            // given
            Long userId = 1L;
            String rawPassword = "password12345!";
            User user = TestUtils.createEntity(User.class, Map.of(
                    "id", userId,
                    "password", "encodedPassword",
                    "isDeleted", false
            ));

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            // when
            authService.withdraw(userId, rawPassword);

            // then
            assertThat(user.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않은 경우.")
        void withdraw_fail_whenInvalidPassword() {
            // given
            Long userId = 1L;
            String rawPassword = "password12345!";
            User user = TestUtils.createEntity(User.class, Map.of(
                    "id", userId,
                    "password", "encodedPassword"
            ));
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            // when
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            // then
            assertThatThrownBy(() -> authService.withdraw(userId, rawPassword))
                    .isInstanceOf(InvalidPasswordException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다.");
        }
    }
}
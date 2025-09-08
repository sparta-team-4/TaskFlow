package com.sparta.taskflow.domain.auth.service;

import com.sparta.taskflow.common.utils.JwtUtils;
import com.sparta.taskflow.domain.auth.dto.request.LoginRequest;
import com.sparta.taskflow.domain.auth.dto.request.UserRegisterRequest;
import com.sparta.taskflow.domain.auth.dto.response.LoginResponse;
import com.sparta.taskflow.domain.auth.dto.response.UserRegisterResponse;
import com.sparta.taskflow.domain.auth.exception.*;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.exception.UserErrorCode;
import com.sparta.taskflow.domain.user.exception.UserNotFoundException;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public UserRegisterResponse userRegister(UserRegisterRequest request) {

        if (isDuplicateUsername(request)) {
            throw new DuplicateUsernameException(AuthErrorCode.DUPLICATE_USERNAME);
        }

        if (isDuplicateEmail(request)) {
            throw new DuplicateEmailException(AuthErrorCode.DUPLICATE_EMAIL);
        }

        return UserRegisterResponse.from(
                userRepository.save(request.toEntity(
                        passwordEncoder.encode(request.password())
                ))
        );
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidUsernameOrPasswordException(AuthErrorCode.INVALID_USERNAME_OR_PASSWORD));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidUsernameOrPasswordException(AuthErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }

        return LoginResponse.of(
                jwtUtils.createToken(user.getId(), user.getRole())
        );
    }

    @Transactional
    public void withdraw(Long userId, String rawPassword) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidPasswordException(AuthErrorCode.INVALID_PASSWORD);
        }

        user.withdraw();
    }

    private boolean isDuplicateUsername(UserRegisterRequest request) {
        return userRepository.countByUsernameAndIsDeletedFalse(request.username()) > 0;
    }

    private boolean isDuplicateEmail(UserRegisterRequest request) {
        return userRepository.countByEmailAndIsDeletedFalse(request.email()) > 0;
    }
}

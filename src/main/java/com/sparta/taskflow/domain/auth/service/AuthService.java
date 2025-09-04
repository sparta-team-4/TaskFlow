package com.sparta.taskflow.domain.auth.service;

import com.sparta.taskflow.domain.auth.dto.request.UserRegisterRequest;
import com.sparta.taskflow.domain.auth.dto.response.UserRegisterResponse;
import com.sparta.taskflow.domain.user.exception.DuplicateUsernameException;
import com.sparta.taskflow.domain.user.exception.UserErrorCode;
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

    @Transactional
    public UserRegisterResponse userRegister(UserRegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUsernameException(UserErrorCode.DUPLICATE_USERNAME);
        }

        return UserRegisterResponse.from(
                userRepository.save(request.toEntity(
                        passwordEncoder.encode(request.password())
                ))
        );
    }
}

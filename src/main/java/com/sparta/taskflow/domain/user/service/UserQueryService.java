package com.sparta.taskflow.domain.user.service;

import com.sparta.taskflow.domain.user.dto.response.UserDetailsResponse;
import com.sparta.taskflow.domain.user.exception.UserErrorCode;
import com.sparta.taskflow.domain.user.exception.UserNotFoundException;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public UserDetailsResponse getUserDetails(Long userId) {
        return UserDetailsResponse.from(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND))
        );
    }
}

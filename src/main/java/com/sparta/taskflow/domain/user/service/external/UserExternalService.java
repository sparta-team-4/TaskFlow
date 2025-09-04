package com.sparta.taskflow.domain.user.service.external;

import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.exception.UserErrorCode;
import com.sparta.taskflow.domain.user.exception.UserNotFoundException;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExternalService {

    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
    }
}

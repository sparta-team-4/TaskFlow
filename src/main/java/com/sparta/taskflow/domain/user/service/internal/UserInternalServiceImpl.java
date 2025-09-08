package com.sparta.taskflow.domain.user.service.internal;

import com.sparta.taskflow.domain.user.dto.UserResponseDto;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.exception.UserErrorCode;
import com.sparta.taskflow.domain.user.exception.UserNotFoundException;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInternalServiceImpl implements UserInternalService {

    private final UserRepository userRepository;

    @Override
    public User getByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public List<UserResponseDto.Simple> searchUsersByQuery(String query) {
        return userRepository.findByNameContainingIgnoreCase(query).stream()
                .map(UserResponseDto.Simple::from)
                .toList();
    }
}

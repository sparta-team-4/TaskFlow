package com.sparta.taskflow.domain.user.service.external;

import com.sparta.taskflow.domain.team.service.internal.TeamInternalService;
import com.sparta.taskflow.domain.user.dto.UserResponseDto;
import com.sparta.taskflow.domain.user.dto.response.UserDetailsResponse;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.exception.UserErrorCode;
import com.sparta.taskflow.domain.user.exception.UserNotFoundException;
import com.sparta.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final TeamInternalService teamInternalService;

    public List<UserResponseDto.Simple> getAvailableUsersForTeam(Long teamId) {
        List<Long> memberIds = teamInternalService.findUserIdsByTeamId(teamId);

        List<User> availableUsers;
        if (memberIds.isEmpty()) {
            availableUsers = userRepository.findAll();
        } else {
            availableUsers = userRepository.findByIdNotIn(memberIds);
        }

        return availableUsers.stream()
                .map(UserResponseDto.Simple::from)
                .collect(Collectors.toList());
    }

    public UserDetailsResponse getUserDetails(Long userId) {
        return UserDetailsResponse.from(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND))
        );
    }
}
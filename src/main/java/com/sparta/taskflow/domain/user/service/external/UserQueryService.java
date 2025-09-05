package com.sparta.taskflow.domain.user.service.external;

import com.sparta.taskflow.domain.team.service.internal.TeamInternalService;
import com.sparta.taskflow.domain.user.dto.UserResponseDto;
import com.sparta.taskflow.domain.user.entity.User;
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
}
package com.sparta.taskflow.domain.team.service.internal;

import com.sparta.taskflow.domain.team.dto.TeamResponseDto;

import java.util.List;
import java.util.Optional;

public interface TeamInternalService {
    List<Long> findUserIdsByTeamId(Long teamId);

    List<TeamResponseDto.Search> searchTeamsByQuery(String query);

    Optional<Long> findTeamIdByUserId(Long userId);
}
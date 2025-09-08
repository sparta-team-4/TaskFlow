package com.sparta.taskflow.domain.team.service.internal;

import com.sparta.taskflow.domain.team.dto.TeamResponseDto;

import java.util.List;

public interface TeamInternalService {
    List<Long> findUserIdsByTeamId(Long teamId);

    List<TeamResponseDto.Search> searchTeamsByQuery(String query);
}
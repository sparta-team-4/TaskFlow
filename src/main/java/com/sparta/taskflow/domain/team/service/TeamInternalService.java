package com.sparta.taskflow.domain.team.service;

import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;

public interface TeamInternalService {
    TeamResponseDto.Create createTeam(TeamRequestDto.Create requestDto);
}
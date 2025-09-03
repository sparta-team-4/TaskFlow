package com.sparta.taskflow.domain.team.service;

import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamInternalService teamInternalService;

    public TeamResponseDto.Create createTeam(TeamRequestDto.Create requestDto){
        return teamInternalService.createTeam(requestDto);
    }
}

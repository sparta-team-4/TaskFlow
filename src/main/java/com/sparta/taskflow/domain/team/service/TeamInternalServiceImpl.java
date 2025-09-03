package com.sparta.taskflow.domain.team.service;

import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamInternalServiceImpl implements TeamInternalService {

    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public TeamResponseDto.Create createTeam(TeamRequestDto.Create requestDto) {
        // 1. 팀 이름 중복 확인
        if (teamRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("팀 이름이 이미 존재합니다");
        }

        // 2. DTO -> Entity 변환
        Team newTeam = requestDto.toEntity();

        // 3. DB에 저장
        Team savedTeam = teamRepository.save(newTeam);

        // 4. Entity -> DTO 변환 후 반환
        return TeamResponseDto.Create.from(savedTeam);
    }
}
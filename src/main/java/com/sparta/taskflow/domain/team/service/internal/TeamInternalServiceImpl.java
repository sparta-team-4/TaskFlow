package com.sparta.taskflow.domain.team.service.internal;

import com.sparta.taskflow.domain.team.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamInternalServiceImpl implements TeamInternalService {

    private final TeamMemberRepository teamMemberRepository;

    @Override
    public List<Long> findUserIdsByTeamId(Long teamId) {
        return teamMemberRepository.findUserIdsByTeamId(teamId);
    }
}
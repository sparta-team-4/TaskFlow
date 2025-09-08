package com.sparta.taskflow.domain.team.service.internal;

import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.repository.TeamMemberRepository;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamInternalServiceImpl implements TeamInternalService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;

    @Override
    public List<Long> findUserIdsByTeamId(Long teamId) {
        return teamMemberRepository.findUserIdsByTeamId(teamId);
    }

    @Override
    public List<TeamResponseDto.Search> searchTeamsByQuery(String query) {
        return teamRepository.findByNameContainingIgnoreCase(query).stream()
                .map(TeamResponseDto.Search::from)
                .toList();
    }

    @Override
    public Optional<Long> findTeamIdByUserId(Long userId) {
        return teamMemberRepository.findByUserId(userId)
                .map(teamMember -> teamMember.getTeam().getId());
    }
}
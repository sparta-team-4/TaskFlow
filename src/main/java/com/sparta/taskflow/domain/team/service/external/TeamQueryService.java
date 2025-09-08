package com.sparta.taskflow.domain.team.service.external;

import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.entity.TeamMember;
import com.sparta.taskflow.domain.team.exception.TeamErrorCode;
import com.sparta.taskflow.domain.team.exception.TeamNotFoundException;
import com.sparta.taskflow.domain.team.repository.TeamMemberRepository;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    /**
     * .
     * @param teamId 조회할 팀의 ID
     * @return 조회된 팀 정보 DTO
     * @throws EntityNotFoundException 해당 ID의 팀이 없을 경우
     */
    public TeamResponseDto.Get getTeam(Long teamId) {
        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TeamErrorCode.TEAM_NOT_FOUND));

        return TeamResponseDto.Get.from(team);
    }

    public List<TeamResponseDto.Get> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        if (teams.isEmpty()) {
            return Collections.emptyList();
        }

        List<TeamMember> allMembers = teamMemberRepository.findByTeamIn(teams);

        Map<Long, List<TeamMember>> membersByTeamId = allMembers.stream()
                .collect(Collectors.groupingBy(tm -> tm.getTeam().getId()));

        return teams.stream()
                .map(team -> TeamResponseDto.Get.from(team, membersByTeamId.getOrDefault(team.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }
}
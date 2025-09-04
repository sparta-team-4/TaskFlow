package com.sparta.taskflow.domain.team.service.external;

import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryService {

    private final TeamRepository teamRepository;

    /**
     * .
     * @param teamId 조회할 팀의 ID
     * @return 조회된 팀 정보 DTO
     * @throws EntityNotFoundException 해당 ID의 팀이 없을 경우
     */
    public TeamResponseDto.Get getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다"));

        return TeamResponseDto.Get.from(team);
    }

    /**
     * 모든 팀의 목록을 페이징하여 조회
     * @param pageable 페이징 정보
     * @return 페이징된 팀 정보 DTO
     */
    public Page<TeamResponseDto.Get> getAllTeams(Pageable pageable) {
        Page<Team> teams = teamRepository.findAll(pageable);
        return teams.map(TeamResponseDto.Get::from);
    }
}
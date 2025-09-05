package com.sparta.taskflow.domain.team.service.external;

import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamCommandService {
    private final TeamRepository teamRepository;

    // 팀 생성
    public TeamResponseDto.Create createTeam(TeamRequestDto.Create requestDto){
        // 팀 이름 중복 확인
        if(teamRepository.existsByName(requestDto.getName())){
            throw new IllegalArgumentException("팀 이름이 이미 존재합니다.");
        }

        // DTO -> Entity 변환
        Team newTeam = requestDto.toEntity();

        // DB에 저장
        Team savedTeam = teamRepository.save(newTeam);

        // Entity -> DTO 변환 후 반환
        return TeamResponseDto.Create.from(savedTeam);
    }

    // 팀 수정
    public TeamResponseDto.Get updateTeam(Long teamId, TeamRequestDto.Update requestDto) {
        // 수정할 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        // 변경하려는 이름이 이미 다른 팀에 의해 사용 중인지 확인
        if (teamRepository.existsByNameAndIdNot(requestDto.getName(), teamId)) {
            throw new IllegalArgumentException("이미 존재하는 팀 이름입니다.");
        }

        // 팀 정보 업데이트
        team.update(requestDto.getName(), requestDto.getDescription());

        // 변경된 팀 정보를 DTO로 변환하여 반환
        return TeamResponseDto.Get.from(team);
    }

    // 팀 삭제
    public void deleteTeam(Long teamId) {
        // 삭제할 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        teamRepository.delete(team);
    }
}
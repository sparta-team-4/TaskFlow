package com.sparta.taskflow.domain.team.service.external;

import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.entity.TeamMember;
import com.sparta.taskflow.domain.team.exception.*;
import com.sparta.taskflow.domain.team.repository.TeamMemberRepository;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.internal.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamCommandService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserInternalService userInternalService;

    // 팀 생성
    public TeamResponseDto.Create createTeam(TeamRequestDto.Create requestDto){
        // 팀 이름 중복 확인
        if(teamRepository.existsByName(requestDto.getName())){
            throw new DuplicateTeamNameException(TeamErrorCode.DUPLICATE_TEAM_NAME);
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
        Team team = findTeamByIdOrThrow(teamId);

        // 변경하려는 이름이 이미 다른 팀에 의해 사용 중인지 확인
        if (teamRepository.existsByNameAndIdNot(requestDto.getName(), teamId)) {
            throw new DuplicateTeamNameException(TeamErrorCode.DUPLICATE_TEAM_NAME);
        }

        // 팀 정보 업데이트
        team.update(requestDto.getName(), requestDto.getDescription());

        // 변경된 팀 정보를 DTO로 변환하여 반환
        return TeamResponseDto.Get.from(team);
    }

    // 팀 삭제
    public void deleteTeam(Long teamId) {
        // 삭제할 팀 조회
        Team team = findTeamByIdOrThrow(teamId);
        teamRepository.delete(team);
    }

    // 팀 멤버 추가
    public TeamResponseDto.Get addMember(Long teamId, TeamRequestDto.AddMember requestDto) {
        // 1. 팀과 유저 엔티티 조회
        Team team = findTeamByIdOrThrow(teamId);
        User user = userInternalService.getUserByIdOrThrow(requestDto.getUserId());

        // 2. 이미 멤버인지 확인
        if (teamMemberRepository.existsByTeamAndUser(team, user)) {
            throw new MemberAlreadyExistsException(TeamErrorCode.MEMBER_ALREADY_EXISTS);
        }

        // 3. TeamMember 엔티티 생성 및 저장
        TeamMember teamMember = new TeamMember(team, user);
        teamMemberRepository.save(teamMember);

        // 4. 변경사항이 적용된 Team 정보를 다시 조회하여 DTO로 반환
        Team updatedTeam = findTeamByIdOrThrow(teamId);

        return TeamResponseDto.Get.from(updatedTeam);
    }

    // 팀 멤버 삭제
    public void deleteMember(Long teamId, Long userId) {
        // 1. 삭제할 팀 멤버 관계를 조회합니다.
        TeamMember teamMember = teamMemberRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new MemberNotFoundException(TeamErrorCode.MEMBER_NOT_FOUND));

        // 2. 조회된 팀 멤버 관계를 삭제합니다.
        teamMemberRepository.delete(teamMember);
    }

    private Team findTeamByIdOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TeamErrorCode.TEAM_NOT_FOUND));
    }
}
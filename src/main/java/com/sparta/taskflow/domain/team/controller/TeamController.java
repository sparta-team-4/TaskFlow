package com.sparta.taskflow.domain.team.controller;

import com.sparta.taskflow.common.response.ApiPageResponse;
import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.service.external.TeamCommandService;
import com.sparta.taskflow.domain.team.service.external.TeamQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;

    // 팀 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponseDto.Create>> createTeam(
            @RequestBody TeamRequestDto.Create requestDto) {
        TeamResponseDto.Create responseDto = teamCommandService.createTeam(requestDto);

        return ApiResponse.created(responseDto, "팀이 성공적으로 생성되었습니다.");
    }

    // 특정 팀 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponseDto.Get>> getTeam(
            @PathVariable Long teamId) {

        TeamResponseDto.Get responseDto = teamQueryService.getTeam(teamId);
        return ApiResponse.success(responseDto, "팀 정보를 조회했습니다.");
    }

    // 팀 전체 조회
    @GetMapping
    public ResponseEntity<ApiPageResponse<TeamResponseDto.Get>> getAllTeams(Pageable pageable) {

        Page<TeamResponseDto.Get> responseDtoPage = teamQueryService.getAllTeams(pageable);
        return ApiPageResponse.success(responseDtoPage, "팀 목록을 조회했습니다.");
    }

    // 팀 수정
    @PutMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponseDto.Get>> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamRequestDto.Update requestDto) {

        TeamResponseDto.Get responseDto = teamCommandService.updateTeam(teamId, requestDto);
        return ApiResponse.success(responseDto, "팀 정보가 성공적으로 업데이트되었습니다.");
    }

    // 팀 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long teamId) {
        teamCommandService.deleteTeam(teamId);
        return ApiResponse.success(null, "팀이 성공적으로 삭제되었습니다.");
    }

    // 팀 멤버 추가
    @PostMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<TeamResponseDto.Get>> addTeamMember(
            @PathVariable Long teamId,
            @RequestBody TeamRequestDto.AddMember requestDto) {

        TeamResponseDto.Get responseDto = teamCommandService.addMember(teamId, requestDto);
        return ApiResponse.success(responseDto, "멤버가 성공적으로 추가되었습니다.");
    }

    // 팀 멤버 삭제
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long userId) {

        teamCommandService.deleteMember(teamId, userId);
        return ApiResponse.success(null, "멤버가 성공적으로 제거되었습니다.");
    }
}
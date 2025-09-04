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

    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponseDto.Create>> createTeam(
            @RequestBody TeamRequestDto.Create requestDto) {
        TeamResponseDto.Create responseDto = teamCommandService.createTeam(requestDto);

        return ApiResponse.created(responseDto, "팀이 성공적으로 생성되었습니다.");
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponseDto.Get>> getTeam(
            @PathVariable Long teamId) {
        TeamResponseDto.Get responseDto = teamQueryService.getTeam(teamId);

        return ApiResponse.success(responseDto, "팀 정보를 조회했습니다.");
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<TeamResponseDto.Get>> getAllTeams(Pageable pageable) {
        Page<TeamResponseDto.Get> responseDtoPage = teamQueryService.getAllTeams(pageable);

        return ApiPageResponse.success(responseDtoPage, "팀 목록을 조회했습니다.");
    }
}

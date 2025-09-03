package com.sparta.taskflow.domain.team.controller;

import com.sparta.taskflow.common.response.ApiResponse;
import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponseDto.Create>> createTeam(
            @RequestBody TeamRequestDto.Create requestDto) {

        // 서비스 로직을 호출하여 팀 생성
        TeamResponseDto.Create responseDto = teamService.createTeam(requestDto);

        // ApiResponse의 정적 메서드를 사용하여 성공 응답 반환
        return ApiResponse.created(responseDto, "팀이 성공적으로 생성되었습니다.");
    }
}

package com.sparta.taskflow.domain.team.service;

import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import com.sparta.taskflow.domain.team.service.external.TeamCommandService;
import com.sparta.taskflow.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamCommandServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamCommandService teamCommandService;

    @Test
    @DisplayName("팀 생성에 성공한다.")
    void createTeam_success() {
        // given
        TeamRequestDto.Create request = new TeamRequestDto.Create(
                "Test Team",
                "This is a test team."
        );

        // TestUtils를 사용하여 save 후 반환될 Team 엔티티 객체 생성
        LocalDateTime createdAt = LocalDateTime.of(2025, 9, 4, 15, 30);
        Team team = TestUtils.createEntity(Team.class, Map.of(
                "id", 1L,
                "name", "Test Team",
                "description", "This is a test team.",
                "createdAt", createdAt
        ));

        given(teamRepository.existsByName(anyString())).willReturn(false);
        given(teamRepository.save(any(Team.class))).willReturn(team);

        // when
        TeamResponseDto.Create response = teamCommandService.createTeam(request);

        // then
        assertThat(response)
                .extracting("id", "name", "description", "createdAt")
                .contains(1L, "Test Team", "This is a test team.", createdAt);

        verify(teamRepository, times(1)).existsByName(anyString());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("이미 존재하는 팀명이 있으면 팀 생성을 할 수 없다.")
    void createTeam_fail_whenDuplicateTeamName() {
        // given
        TeamRequestDto.Create request = new TeamRequestDto.Create(
                "Existing Team",
                "This team already exists."
        );

        // when
        when(teamRepository.existsByName(request.getName())).thenReturn(true);

        // then
        assertThatThrownBy(() -> teamCommandService.createTeam(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("팀 이름이 이미 존재합니다");

        // save 메서드가 호출되지 않았는지 검증
        verify(teamRepository, never()).save(any(Team.class));
    }
}
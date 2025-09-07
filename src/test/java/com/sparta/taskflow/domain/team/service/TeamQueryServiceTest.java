package com.sparta.taskflow.domain.team.service;

import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.entity.TeamMember;
import com.sparta.taskflow.domain.team.exception.TeamNotFoundException;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import com.sparta.taskflow.domain.team.service.external.TeamQueryService;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TeamQueryServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamQueryService teamQueryService;

    @Nested
    @DisplayName("팀 단건 조회")
    class GetTeamTest {
        @Test
        @DisplayName("팀 단건 조회에 성공한다.")
        void getTeam_success() {
            // given
            Long teamId = 1L;
            User user = TestUtils.createEntity(User.class, Map.of("id", 10L, "username", "testuser"));
            Team team = TestUtils.createEntity(Team.class, Map.of("id", teamId, "name", "Test Team"));
            TeamMember teamMember = TestUtils.createEntity(TeamMember.class, Map.of("id", 100L));
            ReflectionTestUtils.setField(teamMember, "user", user);
            ReflectionTestUtils.setField(team, "teamMembers", List.of(teamMember));
            given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

            // when
            TeamResponseDto.Get response = teamQueryService.getTeam(teamId);

            // then
            assertThat(response.getId()).isEqualTo(teamId);
            assertThat(response.getName()).isEqualTo("Test Team");
            assertThat(response.getMembers()).hasSize(1);
            assertThat(response.getMembers().get(0).getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("존재하지 않는 팀 ID로 조회하면 예외가 발생한다.")
        void getTeam_fail_whenTeamNotFound() {
            // given
            Long nonExistentTeamId = 99L;
            given(teamRepository.findById(nonExistentTeamId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamQueryService.getTeam(nonExistentTeamId))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("팀 목록 조회")
    class GetAllTeamsTest {
        @Test
        @DisplayName("팀 목록을 페이징하여 조회한다.")
        void getAllTeams_success() {
            // given
            Team team1 = TestUtils.createEntity(Team.class, Map.of("id", 1L, "name", "Team A"));
            Team team2 = TestUtils.createEntity(Team.class, Map.of("id", 2L, "name", "Team B"));
            Pageable pageable = PageRequest.of(0, 10);
            Page<Team> teamPage = new PageImpl<>(List.of(team1, team2), pageable, 2);
            given(teamRepository.findAll(pageable)).willReturn(teamPage);

            // when
            Page<TeamResponseDto.Get> resultPage = teamQueryService.getAllTeams(pageable);

            // then
            assertThat(resultPage.getTotalElements()).isEqualTo(2);
            assertThat(resultPage.getContent()).hasSize(2);
            assertThat(resultPage.getContent().get(0).getName()).isEqualTo("Team A");
        }
    }
}
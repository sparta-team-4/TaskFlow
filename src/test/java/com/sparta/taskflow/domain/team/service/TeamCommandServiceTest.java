package com.sparta.taskflow.domain.team.service;

import com.sparta.taskflow.domain.team.dto.TeamRequestDto;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.entity.TeamMember;
import com.sparta.taskflow.domain.team.exception.DuplicateTeamNameException;
import com.sparta.taskflow.domain.team.exception.MemberAlreadyExistsException;
import com.sparta.taskflow.domain.team.exception.MemberNotFoundException;
import com.sparta.taskflow.domain.team.exception.TeamNotFoundException;
import com.sparta.taskflow.domain.team.repository.TeamMemberRepository;
import com.sparta.taskflow.domain.team.repository.TeamRepository;
import com.sparta.taskflow.domain.team.service.external.TeamCommandService;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.internal.UserInternalService;
import com.sparta.taskflow.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamCommandServiceTest {

    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TeamMemberRepository teamMemberRepository;
    @Mock
    private UserInternalService userInternalService;

    @InjectMocks
    private TeamCommandService teamCommandService;

    @Nested
    @DisplayName("팀 생성")
    class CreateTeamTest {
        @Test
        @DisplayName("팀 생성에 성공한다.")
        void createTeam_success() {
            // given
            TeamRequestDto.Create request = new TeamRequestDto.Create("Test Team", "Desc");
            LocalDateTime fixedTime = LocalDateTime.of(2025, 9, 7, 11, 0, 0);
            Team team = TestUtils.createEntity(Team.class, Map.of("id", 1L, "name", "Test Team", "description", "Desc", "createdAt", fixedTime));

            given(teamRepository.existsByName(anyString())).willReturn(false);
            given(teamRepository.save(any(Team.class))).willReturn(team);

            // when
            TeamResponseDto.Create response = teamCommandService.createTeam(request);

            // then
            assertThat(response).extracting("id", "name", "description", "createdAt").contains(1L, "Test Team", "Desc", fixedTime);
            verify(teamRepository, times(1)).existsByName(anyString());
            verify(teamRepository, times(1)).save(any(Team.class));
        }

        @Test
        @DisplayName("이미 존재하는 팀명이 있으면 팀 생성을 할 수 없다.")
        void createTeam_fail_whenDuplicateTeamName() {
            // given
            TeamRequestDto.Create request = new TeamRequestDto.Create("Existing Team", "Desc");
            given(teamRepository.existsByName(request.getName())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> teamCommandService.createTeam(request))
                    .isInstanceOf(DuplicateTeamNameException.class);
            verify(teamRepository, never()).save(any(Team.class));
        }
    }

    @Nested
    @DisplayName("팀 수정")
    class UpdateTeamTest {
        @Test
        @DisplayName("팀 정보 수정에 성공한다.")
        void updateTeam_success() {
            // given
            Long teamId = 1L;
            TeamRequestDto.Update request = new TeamRequestDto.Update("Updated Name", "Updated Desc");
            Team team = spy(TestUtils.createEntity(Team.class, Map.of("id", teamId, "name", "Old Name")));

            given(teamRepository.findById(teamId)).willReturn(Optional.of(team));
            given(teamRepository.existsByNameAndIdNot(request.getName(), teamId)).willReturn(false);

            // when
            teamCommandService.updateTeam(teamId, request);

            // then
            verify(team, times(1)).update("Updated Name", "Updated Desc");
        }
    }

    @Nested
    @DisplayName("팀 삭제")
    class DeleteTeamTest {
        @Test
        @DisplayName("팀 삭제에 성공한다.")
        void deleteTeam_success() {
            // given
            Long teamId = 1L;
            Team team = TestUtils.createEntity(Team.class, Map.of("id", teamId));
            given(teamRepository.findById(teamId)).willReturn(Optional.of(team));

            // when
            teamCommandService.deleteTeam(teamId);

            // then
            verify(teamRepository, times(1)).delete(team);
        }

        @Test
        @DisplayName("존재하지 않는 팀이면 예외가 발생한다.")
        void deleteTeam_fail_whenTeamNotFound() {
            // given
            Long teamId = 99L;
            given(teamRepository.findById(teamId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamCommandService.deleteTeam(teamId))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("팀 멤버 추가")
    class AddMemberTest {
        @Test
        @DisplayName("팀 멤버 추가에 성공한다.")
        void addMember_success() {
            // given
            Long teamId = 1L;
            Long userId = 10L;
            TeamRequestDto.AddMember request = new TeamRequestDto.AddMember(userId);
            Team team = TestUtils.createEntity(Team.class, Map.of("id", teamId));
            User user = TestUtils.createEntity(User.class, Map.of("id", userId));

            given(teamRepository.findById(teamId)).willReturn(Optional.of(team));
            given(userInternalService.getUserByIdOrThrow(userId)).willReturn(user);
            given(teamMemberRepository.existsByTeamAndUser(team, user)).willReturn(false);

            // when
            teamCommandService.addMember(teamId, request);

            // then
            verify(teamMemberRepository, times(1)).save(any(TeamMember.class));
        }

        @Test
        @DisplayName("이미 존재하는 멤버이면 예외가 발생한다.")
        void addMember_fail_whenMemberAlreadyExists() {
            // given
            Long teamId = 1L;
            Long userId = 10L;
            TeamRequestDto.AddMember request = new TeamRequestDto.AddMember(userId);
            Team team = TestUtils.createEntity(Team.class, Map.of("id", teamId));
            User user = TestUtils.createEntity(User.class, Map.of("id", userId));

            given(teamRepository.findById(teamId)).willReturn(Optional.of(team));
            given(userInternalService.getUserByIdOrThrow(userId)).willReturn(user);
            given(teamMemberRepository.existsByTeamAndUser(team, user)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> teamCommandService.addMember(teamId, request))
                    .isInstanceOf(MemberAlreadyExistsException.class);
        }
    }

    @Nested
    @DisplayName("팀 멤버 삭제")
    class DeleteMemberTest {
        @Test
        @DisplayName("팀 멤버 삭제에 성공한다.")
        void deleteMember_success() {
            // given
            Long teamId = 1L;
            Long userId = 10L;
            TeamMember teamMember = TestUtils.createEntity(TeamMember.class, Map.of("id", 100L));
            given(teamMemberRepository.findByTeamIdAndUserId(teamId, userId)).willReturn(Optional.of(teamMember));

            // when
            teamCommandService.deleteMember(teamId, userId);

            // then
            verify(teamMemberRepository, times(1)).delete(teamMember);
        }

        @Test
        @DisplayName("존재하지 않는 멤버이면 예외가 발생한다.")
        void deleteMember_fail_whenMemberNotFound() {
            // given
            Long teamId = 1L;
            Long userId = 10L;
            given(teamMemberRepository.findByTeamIdAndUserId(teamId, userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamCommandService.deleteMember(teamId, userId))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }
}
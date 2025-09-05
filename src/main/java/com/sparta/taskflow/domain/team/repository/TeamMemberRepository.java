package com.sparta.taskflow.domain.team.repository;

import com.sparta.taskflow.domain.team.entity.Team;
import com.sparta.taskflow.domain.team.entity.TeamMember;
import com.sparta.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamAndUser(Team team, User user);

    @Query("SELECT TeamMember.user.id from TeamMember WHERE TeamMember.team.id = :teamId")
    List<Long> findUserIdsByTeamId(@Param("teamId") Long teamId);
}

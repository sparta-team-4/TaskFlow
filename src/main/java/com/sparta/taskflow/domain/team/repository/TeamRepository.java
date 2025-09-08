package com.sparta.taskflow.domain.team.repository;

import com.sparta.taskflow.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.teamMembers tm LEFT JOIN FETCH tm.user WHERE t.id = :teamId")
    Optional<Team> findByIdWithMembers(@Param("teamId") Long teamId);

    List<Team> findByNameContainingIgnoreCase(String query);
}
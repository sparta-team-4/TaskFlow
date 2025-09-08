package com.sparta.taskflow.domain.search.dto.response;

import com.sparta.taskflow.domain.task.dto.TaskResponse;
import com.sparta.taskflow.domain.team.dto.TeamResponseDto;
import com.sparta.taskflow.domain.user.dto.UserResponseDto;

import java.util.List;

public record SearchResponse(
        List<TaskResponse> tasks,
        List<UserResponseDto.Simple> users,
        List<TeamResponseDto.Search> teams
) {

    public static SearchResponse of(List<TaskResponse> tasks,
                                    List<UserResponseDto.Simple> users,
                                    List<TeamResponseDto.Search> teams) {
        return new SearchResponse(tasks, users, teams);
    }
}

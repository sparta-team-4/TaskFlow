package com.sparta.taskflow.domain.dashboard.dto.response;

import com.sparta.taskflow.domain.task.repository.dto.TeamProgressProjection;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class DashboardResponseDto {

    private final Map<String, Integer> teamProgress;

    private DashboardResponseDto(Map<String, Integer> teamProgress) {
        this.teamProgress = teamProgress;
    }

    public static DashboardResponseDto from(List<TeamProgressProjection> projections) {
        Map<String, Integer> progressMap = projections.stream()
                .collect(Collectors.toMap(
                        TeamProgressProjection::getTeamName,
                        projection -> {
                            if (projection.getTotalTasks() == 0) {
                                return 0;
                            }
                            return (int) Math.round(((double) projection.getCompletedTasks() / projection.getTotalTasks()) * 100);
                        }
                ));
        return new DashboardResponseDto(progressMap);
    }
}
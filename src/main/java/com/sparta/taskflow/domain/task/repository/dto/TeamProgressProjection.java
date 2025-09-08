package com.sparta.taskflow.domain.task.repository.dto;

import lombok.Getter;

@Getter
public class TeamProgressProjection {
    private final String teamName;
    private final long totalTasks;
    private final long completedTasks;

    public TeamProgressProjection(String teamName, long totalTasks, long completedTasks) {
        this.teamName = teamName;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
    }
}
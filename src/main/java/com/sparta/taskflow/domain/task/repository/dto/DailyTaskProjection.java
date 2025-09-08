package com.sparta.taskflow.domain.task.repository.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDate;

public record DailyTaskProjection(
        LocalDate date,
        int totalTasks,
        int completedTasks
) {
    @QueryProjection
    public DailyTaskProjection {
    }

    public static final int DEFAULT_TOTAL_TASKS = 0;
    public static final int DEFAULT_COMPLETED_TASKS = 0;

    public static DailyTaskProjection of(LocalDate date, int totalTasks, int completedTasks) {
        return new DailyTaskProjection(date, totalTasks, completedTasks);
    }

    public static DailyTaskProjection empty(LocalDate date) {
        return new DailyTaskProjection(date, DEFAULT_TOTAL_TASKS, DEFAULT_COMPLETED_TASKS);
    }
}

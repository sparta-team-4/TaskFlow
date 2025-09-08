package com.sparta.taskflow.domain.dashboard.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record WeeklyTrendResponse(
        String name,
        int tasks,
        int completed,
        LocalDate date
) {

    public static WeeklyTrendResponse of(String name, int tasks, int completed, LocalDate date) {
        return WeeklyTrendResponse.builder()
                .name(name)
                .tasks(tasks)
                .completed(completed)
                .date(date)
                .build();
    }
}

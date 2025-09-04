package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.entity.Task;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

public class DataDto {

    private final List<Task> data;
    private final int totalElements;
    private final int totalPages;
    private final int number;
    private int size;

    @Builder
    public DataDto(List<Task> data,
                   int totalElements,
                   int totalPages,
                   int size,
                   int number) {
        this.data = data;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;

    }

    public static DataDto data(Page<Task> taskPage) {
        return DataDto.builder()
                .data(taskPage.getContent())
                .totalElements((int) taskPage.getTotalElements())
                .totalPages(taskPage.getTotalPages())
                .number(taskPage.getNumber())
                .size(10)
                .build();
    }
}

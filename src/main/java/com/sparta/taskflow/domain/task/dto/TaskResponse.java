package com.sparta.taskflow.domain.task.dto;

import com.sparta.taskflow.domain.task.entity.Task;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class TaskResponse {
    private boolean success;
    private String message;
    private final Task data;
    private LocalDateTime timestamp;

    @Builder
    private TaskResponse(boolean success,
                        String message,
                        Task data,
                        LocalDateTime timestamp) {
        this.data = data;
    }

    //생성 응답
    public static TaskResponse response(Task data){
        return TaskResponse.builder()
                .success(true)
                .message("Task가 생성되었습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    //수정 응답
    public static TaskResponse updateResponse(Task data){
        return TaskResponse.builder()
                .success(true)
                .message("Task가 수정되었습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    //상태 수정 응답
    public static TaskResponse updateStatusResponse(Task data){
        return TaskResponse.builder()
                .success(true)
                .message("작업 상태가 업데이트되었습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    //상세 조회 응답
    public static TaskResponse getByIdResponse(Task data){
        return TaskResponse.builder()
                .success(true)
                .message("Task를 조회했습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    //삭제 응답
    public static TaskResponse deleteResponse(Task data){
        return TaskResponse.builder()
                .success(true)
                .message("Task를 조회했습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

package com.sparta.taskflow.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiPageResponse<T> {

    private final boolean success;
    private final String message;
    private final PageData<T> data;
    private final LocalDateTime timestamp;

    private ApiPageResponse(PageData<T> data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    @Getter
    private static class PageData<T> {
        private final List<T> content;
        private final long totalElements;
        private final int totalPages;
        private final int size;
        private final int number;

        @Builder
        private PageData(List<T> content, long totalElements, int totalPages, int size, int number) {
            this.content = content;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.size = size;
            this.number = number;
        }
    }

    /**
     * 성공적인 요청에 대한 페이징 응답을 반환하는 메서드
     * 주어진 데이터를 포함하여 HTTP 200 OK 상태 코드와 함께 응답을 반환
     *
     * @param pagedData 요청 성공 시 반환할 페이징 데이터
     * @return HTTP 200 OK 응답과 함께 성공 데이터가 포함된 ApiPageResponse
     */
    public static <T> ResponseEntity<ApiPageResponse<T>> success(Page<T> pagedData, String message) {
        return ResponseEntity.ok(fromPage(pagedData, message));
    }

    private static <T> ApiPageResponse<T> fromPage(Page<T> pagedData, String message) {
        return new ApiPageResponse<>(
                buildPageData(pagedData),
                message
        );
    }

    private static <T> PageData<T> buildPageData(Page<T> pagedData) {
        return PageData.<T>builder()
                .content(pagedData.getContent())
                .totalElements(pagedData.getTotalElements())
                .totalPages(pagedData.getTotalPages())
                .size(pagedData.getSize())
                .number(pagedData.getNumber())
                .build();
    }
}

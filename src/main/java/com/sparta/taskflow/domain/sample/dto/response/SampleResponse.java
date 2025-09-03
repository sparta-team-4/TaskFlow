package com.sparta.taskflow.domain.sample.dto.response;

import lombok.Getter;

@Getter
public class SampleResponse {

    private final String message;
    private final String data;

    public SampleResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }
}

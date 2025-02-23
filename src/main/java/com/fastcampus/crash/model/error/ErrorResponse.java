package com.fastcampus.crash.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(int status, String message) {
    public ErrorResponse(HttpStatus status, String message) {
        this(status.value(), message); // 숫자로 변환
    }
}
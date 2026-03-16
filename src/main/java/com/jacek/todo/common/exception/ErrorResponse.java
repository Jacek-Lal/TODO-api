package com.jacek.todo.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Error details returned when a request fails")
public record ErrorResponse(

        @Schema(description = "HTTP status code")
        int status,

        @Schema(description = "Descriptive error message")
        String message,

        @Schema(description = "Timestamp when the error occurred")
        Instant timestamp) {
}

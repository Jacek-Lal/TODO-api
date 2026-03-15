package com.jacek.todo.common.exception;

import java.time.Instant;

public record ErrorResponse(int status, String message, Instant timestamp) {
}

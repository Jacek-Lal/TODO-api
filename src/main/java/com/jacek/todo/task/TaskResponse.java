package com.jacek.todo.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Task details returned by the API")
public record TaskResponse(

        @Schema(description = "Unique identifier of the task")
        Long id,

        String title,
        String description,
        TaskStatus status,

        @Schema(description = "UTC timestamp of task creation")
        Instant createdAt
) { }

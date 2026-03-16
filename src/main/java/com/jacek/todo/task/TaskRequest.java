package com.jacek.todo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for creating or updating a task")
public record TaskRequest(

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Title is required")
        String title,

        @Schema(description = "Optional detailed description of the task")
        String description,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Status is required")
        TaskStatus status
) { }

package com.jacek.todo.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(@NotBlank(message = "Title is required") String title,
                          String description,
                          @NotNull(message = "Status is required") TaskStatus status) {
}

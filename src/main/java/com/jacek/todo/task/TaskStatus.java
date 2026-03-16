package com.jacek.todo.task;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Current status of the task")
public enum TaskStatus {
    @Schema(description = "Task has been created but not started")
    NEW,

    @Schema(description = "Task is currently being worked on")
    IN_PROGRESS,

    @Schema(description = "Task has been completed")
    DONE
}

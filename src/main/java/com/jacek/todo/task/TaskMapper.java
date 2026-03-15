package com.jacek.todo.task;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskRequest request);
    TaskResponse toResponse(Task task);
}

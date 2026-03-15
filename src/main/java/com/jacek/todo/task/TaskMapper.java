package com.jacek.todo.task;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskRequest request);
    TaskResponse toResponse(Task task);
    void updateEntity(TaskRequest request, @MappingTarget Task task);
}

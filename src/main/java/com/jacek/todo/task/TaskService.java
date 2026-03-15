package com.jacek.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponse createTask(TaskRequest request) {
        Task newtask = taskMapper.toEntity(request);
        Task savedTask = taskRepository.save(newtask);
        return taskMapper.toResponse(savedTask);
    }

}


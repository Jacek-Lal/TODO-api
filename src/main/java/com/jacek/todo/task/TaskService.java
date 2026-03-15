package com.jacek.todo.task;

import com.jacek.todo.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<TaskResponse> getTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(taskMapper::toResponse);
    }

    public TaskResponse getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task with id " + id + " not found"));

        return taskMapper.toResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task with id " + id + " not found"));

        taskMapper.updateEntity(request, task);
        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }
}


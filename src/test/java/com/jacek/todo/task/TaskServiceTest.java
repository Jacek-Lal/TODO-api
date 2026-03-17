package com.jacek.todo.task;

import com.jacek.todo.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("should create task and return response")
    void shouldCreateTask() {
        TaskRequest request = new TaskRequest("title", "desc", TaskStatus.NEW);
        Task entity = new Task("title", "desc", TaskStatus.NEW);
        Task saved = new Task("title", "desc", TaskStatus.NEW);
        TaskResponse expected = new TaskResponse(1L, "title", "desc", TaskStatus.NEW, null);

        when(taskMapper.toEntity(request)).thenReturn(entity);
        when(taskRepository.save(entity)).thenReturn(saved);
        when(taskMapper.toResponse(saved)).thenReturn(expected);

        TaskResponse result = taskService.createTask(request);

        assertThat(result).isEqualTo(expected);
        verify(taskRepository).save(entity);
    }

    @Test
    @DisplayName("should return page of tasks")
    void shouldReturnPageOfTasks() {
        Pageable pageable = PageRequest.of(0, 20);
        Task task = new Task("title", "desc", TaskStatus.NEW);
        TaskResponse response = new TaskResponse(1L, "title", "desc", TaskStatus.NEW, null);
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(pageable)).thenReturn(page);
        when(taskMapper.toResponse(task)).thenReturn(response);

        Page<TaskResponse> result = taskService.getTasks(pageable);

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("should return task by id")
    void shouldReturnTaskById() {
        Task task = new Task("title", "desc", TaskStatus.NEW);
        TaskResponse expected = new TaskResponse(1L, "title", "desc", TaskStatus.NEW, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(expected);

        TaskResponse result = taskService.getTask(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when task does not exist")
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task with id 99 not found");
    }

    @Test
    @DisplayName("should update task and return updated response")
    void shouldUpdateTask() {
        TaskRequest request = new TaskRequest("new title", "new desc", TaskStatus.IN_PROGRESS);
        Task task = new Task("title", "desc", TaskStatus.NEW);
        TaskResponse expected = new TaskResponse(1L, "new title", "new desc", TaskStatus.IN_PROGRESS, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(expected);

        TaskResponse result = taskService.updateTask(1L, request);

        assertThat(result).isEqualTo(expected);
        verify(taskMapper).updateEntity(request, task);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when updating non-existent task")
    void shouldThrowWhenUpdatingNonExistentTask() {
        TaskRequest request =  new TaskRequest("title", "desc", TaskStatus.NEW);

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task with id 99 not found");
    }

    @Test
    @DisplayName("should delete task")
    void shouldDeleteTask() {
        Task task = new Task("title", "desc", TaskStatus.NEW);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when deleting non-existent task")
    void shouldThrowWhenDeletingNonExistentTask() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task with id 99 not found");
    }
}
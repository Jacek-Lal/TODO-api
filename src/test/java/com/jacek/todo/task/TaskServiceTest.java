package com.jacek.todo.task;

import com.jacek.todo.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("should throw ResourceNotFoundException when task does not exist")
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task with id 99 not found");
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

}
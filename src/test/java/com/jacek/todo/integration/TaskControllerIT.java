package com.jacek.todo.integration;

import com.jacek.todo.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class TaskControllerIT {

    private RestTestClient client;

    @Autowired
    private TaskRepository taskRepository;

    private Long testTaskId;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context)
                .baseUrl("http://localhost:8080/api/tasks")
                .build();

        taskRepository.deleteAll();

        Task task = new Task("test task", "test desc", TaskStatus.NEW);
        testTaskId = taskRepository.save(task).getId();
    }

    @Test
    @DisplayName("should create task and return 201 with location header")
    void shouldCreateTask(){

    }

    @Test
    @DisplayName("should return 400 when creating task with blank title")
    void shouldReturn400WhenBlankTitle(){

    }

    @Test
    @DisplayName("should return all tasks with pagination")
    void shouldReturnAllTasks(){

    }

    @Test
    @DisplayName("should return task by id")
    void shouldReturnTaskById(){

    }

    @Test
    @DisplayName("should return 404 when task not found")
    void shouldReturn404WhenTaskNotFound(){

    }

    @Test
    @DisplayName("should update task and return updated response")
    void shouldUpdateTask(){

    }

    @Test
    @DisplayName("should delete task and return 204")
    void shouldDeleteTask(){

    }
}

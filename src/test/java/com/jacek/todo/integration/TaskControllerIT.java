package com.jacek.todo.integration;

import com.jacek.todo.common.exception.ErrorResponse;
import com.jacek.todo.task.*;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
class TaskControllerIT {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RestTestClient client;
    private Long testTaskId;

    @BeforeEach
    void setUp(){
        taskRepository.deleteAll();

        Task task = new Task("test task", "test desc", TaskStatus.NEW);
        testTaskId = taskRepository.save(task).getId();
    }

    @Test
    @DisplayName("should create task and return 201 with location header")
    void shouldCreateTask(){
        TaskResponse response = client.post()
                .uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TaskRequest("new task", "desc", TaskStatus.NEW))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().value("Location", location ->
                        assertThat(location).contains("/api/tasks/"))
                .expectBody(TaskResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(new TaskResponse(null, "new task", "desc", TaskStatus.NEW, null));
        assertThat(response.id()).isNotNull();
        assertThat(response.createdAt()).isNotNull();

        assertThat(taskRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("should return 400 when creating task with blank title")
    void shouldReturn400WhenBlankTitle(){
        ErrorResponse error = client.post()
                .uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TaskRequest("", "desc", TaskStatus.NEW))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(error).isNotNull();
        assertThat(error.status()).isEqualTo(400);
        assertThat(error.message()).isNotBlank();
    }

    @Test
    @DisplayName("should return all tasks with pagination")
    void shouldReturnAllTasks() throws JSONException {
        String response = client.get()
                .uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        JSONAssert.assertEquals("""
                {
                    "content": [
                        {
                            "id": %d,
                            "title": "test task",
                            "description": "test desc",
                            "status": "NEW"
                        }
                    ],
                    "page": {
                        "totalElements": 1,
                        "totalPages": 1
                    }
                }
                """.formatted(testTaskId), response, JSONCompareMode.LENIENT);
    }

    @Test
    @DisplayName("should return task by id")
    void shouldReturnTaskById(){
        TaskResponse response = client.get()
                .uri("/api/tasks/{id}", testTaskId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(testTaskId);
        assertThat(response.title()).isEqualTo("test task");
        assertThat(response.description()).isEqualTo("test desc");
        assertThat(response.status()).isEqualTo(TaskStatus.NEW);
    }

    @Test
    @DisplayName("should return 404 when task not found")
    void shouldReturn404WhenTaskNotFound(){
        ErrorResponse error = client.get()
                .uri("/api/tasks/{id}", 999)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(error).isNotNull();
        assertThat(error.status()).isEqualTo(404);
        assertThat(error.message()).isNotBlank();
    }

    @Test
    @DisplayName("should update task and return updated response")
    void shouldUpdateTask(){
        TaskResponse response = client.put()
                .uri("/api/tasks/{id}", testTaskId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TaskRequest("updated task", "updated desc", TaskStatus.IN_PROGRESS))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(testTaskId);
        assertThat(response.title()).isEqualTo("updated task");
        assertThat(response.description()).isEqualTo("updated desc");
        assertThat(response.status()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("should return 404 when updating non-existent task")
    void shouldReturn404WhenUpdatingNonExistentTask(){
        client.put()
                .uri("/api/tasks/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TaskRequest("updated task", "updated desc", TaskStatus.IN_PROGRESS))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("should delete task and return 204")
    void shouldDeleteTask(){
        client.delete()
                .uri("/api/tasks/{id}", testTaskId)
                .exchange()
                .expectStatus().isNoContent();

        assertThat(taskRepository.findById(testTaskId)).isEmpty();
    }

    @Test
    @DisplayName("should return 404 when deleting non-existent task")
    void shouldReturn404WhenDeletingNonExistentTask(){
        client.delete()
                .uri("/api/tasks/{id}", 999)
                .exchange()
                .expectStatus().isNotFound();
    }
}

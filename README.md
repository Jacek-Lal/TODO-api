# Todo API

Simple task management REST API built with Spring Boot.
Allows clients to create, read, update, and delete tasks, with support for pagination and sorting. 

## Tech Stack

- **Language & Framework**: Java 21, Spring Boot 4 
- **Build Tool**: Maven
- **Persistence**: Spring Data JPA, PostgreSQL, Flyway 
- **Utilities**: MapStruct, Lombok 
- **Documentation**: Springdoc OpenAPI (Swagger UI) 
- **Infrastructure**: Docker 
- **Testing**: JUnit5, Mockito, Testcontainers
- **Version Control**: Git

---

## Running the Application

### Prerequisites
- Docker Engine/Docker Desktop installed and running (for both options)
- Java 21+ installed (for local development)

### Option 1 - Dockerized application

```bash
docker compose up -d
```

### Option 2 - Local development

Start the database:

```bash
docker compose -f docker-compose.dev.yml up -d
```

Run the application:

```bash
./mvnw spring-boot:run
```

Application starts at `http://localhost:8080`  
Swagger UI available at `http://localhost:8080/swagger-ui.html`

---

## Running Tests

### Unit tests:

```bash
./mvnw test
```

### Unit + integration tests (requires Docker):

```bash
./mvnw verify
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tasks` | Create a new task |
| GET | `/api/tasks` | Get all tasks (paginated) |
| GET | `/api/tasks/{id}` | Get task by ID |
| PUT | `/api/tasks/{id}` | Update task by ID |
| DELETE | `/api/tasks/{id}` | Delete task by ID |

### Pagination

`GET /api/tasks` supports the following query parameters:

| Parameter | Default | Description |
|-----------|---------|-------------|
| `page` | `0` | Page number (0-based) |
| `size` | `20` | Items per page (max 100) |
| `sort` | `createdAt,desc` | Sort field and direction |

Example: `GET /api/tasks?page=0&size=10&sort=createdAt,desc`
package org.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.project.model.Task;
import org.project.repository.TaskRepository;
import org.project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Контроллер для управления задачами.
 * Обрабатывает CRUD-операции с задачами через веб-интерфейс.
 *
 * @author GhostASA5
 * @version 1.0
 * @see Task
 * @see TaskRepository
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Controller", description = "REST API для операций с задачами")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Получить все задачи")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новую задачу")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

package org.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.project.model.Task;
import org.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * Контроллер для управления задачами.
 * Обрабатывает CRUD-операции с задачами через веб-интерфейс.
 *
 * @author GhostASA5
 * @version 1.0
 * @see Task
 * @see TaskRepository
 */
@Controller
@RequestMapping("/tasks")
@Tag(name = "Task Controller", description = "Операции с задачами")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Отображает список всех задач.
     *
     * @param model модель для передачи данных в представление
     * @return имя Thymeleaf шаблона "tasks"
     */
    @GetMapping
    @Operation(summary = "Получить все задачи", description = "Возвращает список всех задач")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка задач")
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "tasks";
    }

    /**
     * Отображает форму добавления новой задачи.
     *
     * @param model модель для передачи данных в представление
     * @return имя Thymeleaf шаблона "add-task"
     */
    @Operation(summary = "Добавить задачу", description = "Создает новую задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные задачи")
    })
    @GetMapping("/add")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "add-task";
    }

    /**
     * Обрабатывает отправку формы добавления задачи.
     *
     * @param task объект задачи из формы
     * @return редирект на страницу списка задач
     */
    @PostMapping("/add")
    public String addTask(@RequestBody
                          @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные новой задачи") Task task) {
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи для удаления
     * @return редирект на страницу списка задач
     */
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PostMapping("/delete/{id}")
    public String deleteTask(@Parameter(description = "ID задачи для удаления", required = true)
                                 @PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}

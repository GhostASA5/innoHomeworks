package org.project.controller;

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
    public String addTask(@ModelAttribute Task task) {
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
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}

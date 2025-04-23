package org.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.project.model.Task;
import org.project.repository.LogEntryRepository;
import org.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private LogEntryRepository logEntryRepository;

    @Test
    @DisplayName("GET /tasks - должен вернуть страницу задач")
    void getAllTasks_ShouldReturnTasksPage() throws Exception {
        // Подготовка моков
        when(taskRepository.findAll()).thenReturn(Arrays.asList(
                new Task(1L, "Task 1", "Description 1", LocalDateTime.now()),
                new Task(2L, "Task 2", "Description 2", LocalDateTime.now())
        ));

        // Выполнение запроса и проверки
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("tasks", hasSize(2)));
    }

    @Test
    @DisplayName("GET /tasks/add - должен вернуть форму добавления задачи")
    void showAddTaskForm_ShouldReturnAddTaskPage() throws Exception {
        mockMvc.perform(get("/tasks/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-task"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    @DisplayName("POST /tasks/add - должен сохранить задачу и сделать редирект")
    void addTask_ShouldSaveTaskAndRedirect() throws Exception {
        Task newTask = new Task(null, "New Task", "New Desc", null);

        mockMvc.perform(post("/tasks/add")
                        .flashAttr("task", newTask))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/tasks"));

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());

        assertNotNull(taskCaptor.getValue().getCreatedAt());
        assertEquals("New Task", taskCaptor.getValue().getName());
    }

    @Test
    @DisplayName("POST /tasks/delete/{id} - должен удалить задачу")
    void deleteTask_ShouldDeleteTask() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(post("/tasks/delete/" + taskId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskRepository).deleteById(taskId);
    }
}
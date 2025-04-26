package org.project.repository;

import org.junit.jupiter.api.Test;
import org.project.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TaskRepositoryH2Test {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldSaveAndRetrieveTask() {
        Task task = new Task();
        task.setName("H2 Test Task");
        task.setDescription("H2 Description");
        task.setCreatedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);
        Optional<Task> found = taskRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("H2 Test Task", found.get().getName());
    }

    @Test
    void shouldFindAllTasks() {
        taskRepository.save(new Task("Task 1", "Desc 1"));
        taskRepository.save(new Task("Task 2", "Desc 2"));

        List<Task> tasks = taskRepository.findAll();
        assertEquals(2, tasks.size());
    }
}
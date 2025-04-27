package org.project.service;

import org.project.model.Task;
import org.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Cacheable(value = "tasks", unless = "#result == null or #result.isEmpty()")
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Cacheable(value = "task", key = "#id", unless = "#result == null")
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @CachePut(value = "task", key = "#id")
    @CacheEvict(value = "tasks", allEntries = true)
    public Task updateTask(Long id, Task task) {
        task.setId(id);
        return taskRepository.save(task);
    }

    @CacheEvict(value = {"tasks", "task"}, key = "#id")
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
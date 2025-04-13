package org.project.repository;

import org.project.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link Task}.
 * Наследует стандартные CRUD-операции из JpaRepository.
 *
 * @author GhostASA5
 * @version 1.0
 * @see Task
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
}

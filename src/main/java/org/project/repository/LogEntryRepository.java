package org.project.repository;

import org.project.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link LogEntry}.
 * Предоставляет стандартные CRUD-операции для записей лога.
 *
 * @author GhostASA5
 * @version 1.0
 * @see LogEntry
 */
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}

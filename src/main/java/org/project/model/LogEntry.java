package org.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * Сущность для хранения записей лога HTTP-запросов.
 * Содержит информацию о методе запроса, URI, заголовке авторизации и времени события.
 *
 * @author GhostASA5
 * @version 1.0
 */
@Entity
@Table(name = "log_entries")
@Getter
@Setter
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;

    private String uri;

    private String authHeader;

    private LocalDateTime timestamp;

}

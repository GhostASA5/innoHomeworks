package org.project.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * Сущность задачи в системе.
 * Содержит название, описание и дату создания задачи.
 *
 * @author GhostASA5
 * @version 1.0
 */
@Entity
@Getter
@Setter
@Schema(description = "Сущность задачи")
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Название задачи", example = "Сделать домашнее задание", required = true)
    private String name;

    @Schema(description = "Описание задачи", example = "Домашнее задание по математике", required = true)
    private String description;

    @Schema(description = "Дата создания задачи", example = "12.04.2025", required = true)
    private LocalDateTime createdAt;
}

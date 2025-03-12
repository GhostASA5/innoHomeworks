package org.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KafkaMessage  {

    private Long id;

    private String message;

    private LocalDateTime sendTime;

}

package org.project.notification.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Student {

    private Long id;

    private String fio;

    private String email;

    private LocalDate birthDay;

    private Long courseId;
}

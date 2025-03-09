package org.project.notification.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Course {

    private Long id;

    private String courseName;

    private LocalDate startDate;

    private Boolean activeStatus;
}

package org.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.text.MessageFormat;
import java.time.LocalDate;


@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private LocalDate startDate;

    private Boolean activeStatus;

    public String toString() {
        return MessageFormat.format("Course Name: {0}", courseName);
    }
}

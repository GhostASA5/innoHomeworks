package org.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.text.MessageFormat;
import java.time.LocalDate;


@Entity
@Table(name = "archived_courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchivedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private LocalDate startDate;

    public String toString() {
        return MessageFormat.format("Archived course Name: {0}", courseName);
    }
}

package org.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_to_student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseToStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private Long courseId;
}

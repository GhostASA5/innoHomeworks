package org.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "earthquake")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Earthquake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime time;

    private Double magnitude;

    private String place;
}

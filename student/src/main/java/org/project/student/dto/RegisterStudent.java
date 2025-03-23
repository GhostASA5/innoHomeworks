package org.project.student.dto;

import lombok.Data;
import org.project.student.model.RoleType;

import java.time.LocalDate;


@Data
public class RegisterStudent {

    private String fio;

    private String email;

    private String password;

    private RoleType roleType;

    private LocalDate birthDay;

}

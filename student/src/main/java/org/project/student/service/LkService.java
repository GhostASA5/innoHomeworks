package org.project.student.service;

import lombok.RequiredArgsConstructor;
import org.project.model.Course;
import org.project.student.model.Student;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LkService {

    private final StudentService studentService;

    public List<Course> getCourses(String email) {
        Student student = studentService.getStudentByEmail(email);
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri("http://localhost:8080/api/courses/students/" + student.getId())
                .retrieve()
                .body(List.class);
    }
}

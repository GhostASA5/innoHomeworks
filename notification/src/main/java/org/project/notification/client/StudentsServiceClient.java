package org.project.notification.client;

import org.project.notification.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class StudentsServiceClient {

    public List<Student> getStudentsByCourseId(Long courseId) {
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri("http://localhost:8088/api/students/course/" + courseId)
                .retrieve()
                .body(List.class);
    }
}

package org.project.notification.client;

import org.project.notification.model.Course;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CourseServiceClient {

    public List<Course> getAllCourses() {
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri("http://localhost:8080/api/courses")
                .retrieve()
                .body(List.class);
    }

    public void archiveCourse(Long courseId) {
        RestClient restClient = RestClient.create();
        restClient.post()
                .uri("http://localhost:8080/api/courses/archive/" + courseId);
    }
}

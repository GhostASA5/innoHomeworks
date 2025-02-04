package org.project.api;

import lombok.RequiredArgsConstructor;
import org.project.model.Course;
import org.project.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping
    public ResponseEntity<Void> createCourse(@RequestBody Course course) {
        courseService.addCourse(course);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/{courseId}")
    public ResponseEntity<Void> registerStudent(@RequestParam Long id,
                                                @PathVariable Long courseId) {
        courseService.registerStudentOnCourse(id, courseId);
        return ResponseEntity.ok().build();
    }
}

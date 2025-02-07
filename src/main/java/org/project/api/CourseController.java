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

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createCourse(@RequestBody Course course) {
        courseService.addCourse(course);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable long id, @RequestBody Course course) {
        courseService.updateCourse(id, course);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}/{status}")
    public ResponseEntity<Void> deleteCourse(@PathVariable long id, @PathVariable boolean status) {
        courseService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }


}

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

    @GetMapping("/student/{id}")
    public ResponseEntity<List<Course>> getCoursesByStudentId(@PathVariable long id) {
        return ResponseEntity.ok(courseService.getStudentCourses(id));
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<Boolean> checkCourse(@PathVariable long id) {
        try {
            courseService.getCourseById(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/multipleCourses")
    public List<Long> getMultipleCourses() {
        return courseService.getStudentsWith2AndMoreCourses();
    }

    @PostMapping
    public ResponseEntity<Void> createCourse(@RequestBody Course course) {
        courseService.addCourse(course);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/archive/{courseId}")
    public ResponseEntity<Void> archiveCourse(@PathVariable long courseId) {
        courseService.archive(courseId);
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

package org.project.student.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.student.model.Student;
import org.project.student.student.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Student>> getStudentsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.getStudentsByCourseId(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createStudent(@RequestBody @Valid Student student) {
        studentService.createStudent(student);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/course/{courseId}/{studentId}")
    public ResponseEntity<Void> updateStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            studentService.registerStudentOnCourse(courseId, studentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody @Valid Student student) {
        return  ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteNote(id);
        return ResponseEntity.ok().build();
    }
}

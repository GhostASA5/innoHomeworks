package org.project.student.student;

import lombok.RequiredArgsConstructor;
import org.project.student.exception.StudentNotFoundException;
import org.project.student.model.Student;
import org.project.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(
                MessageFormat.format("Student with id {0} not found", id)
        ));
    }

    public void createStudent(Student student) {
        studentRepository.save(student);
    }

    public void registerStudentOnCourse(Long courseId, Long studentId) {
        Student student = getStudentById(studentId);
        RestClient restClient = RestClient.create();
        Boolean checkCourseExist = restClient.get()
                .uri("http://localhost:8080/api/courses/check/" + courseId)
                .retrieve()
                .body(Boolean.class);

        if (Boolean.TRUE.equals(checkCourseExist)) {
            student.setCourseId(courseId);
            studentRepository.save(student);
        } else {
            throw new RuntimeException("Course not found");
        }
    }

    public Student updateStudent(Long id, Student student) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student studentToUpdate = studentOptional.get();
            studentToUpdate.setEmail(student.getEmail());
            studentRepository.save(studentToUpdate);

            return studentToUpdate;
        }
        throw new StudentNotFoundException(MessageFormat.format("Student with id {0} not found", id));
    }

    public void deleteNote(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            studentRepository.deleteById(id);
        } else {
            throw new StudentNotFoundException(MessageFormat.format("Student with id {0} not found", id));
        }
    }
}
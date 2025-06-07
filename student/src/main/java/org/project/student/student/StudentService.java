package org.project.student.student;

import lombok.RequiredArgsConstructor;
import org.project.student.aop.LogSpendTime;
import org.project.student.exception.StudentNotFoundException;
import org.project.student.model.Student;
import org.project.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @LogSpendTime
    public List<Student> getStudentsByCourseId(Long courseId) {
        return studentRepository.findByCourseId(courseId);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(
                MessageFormat.format("Student with id {0} not found", id)
        ));
    }

    public List<Student> getStudentsOlderThan(Integer age) {
        LocalDate cutoffDate = LocalDate.now().minusYears(age);
        return studentRepository.findStudentsOlderThan(cutoffDate);
    }

    public List<Student> getStudentsByCountry(String country) {
        return studentRepository.findByCountry(country);
    }

    public List<Student> getStudentsWith2OrMoreCourses() {
        RestClient restClient = RestClient.create();
        List getStudentsIds = restClient.get()
                .uri("http://localhost:8080/api/courses/multipleCourses")
                .retrieve()
                .body(List.class);
        List<Student> students = getStudentsIds.stream()
                .map(i -> getStudentById((Long) i))
                .toList();
        return students;
    }

    @LogSpendTime
    public void createStudent(Student student) {
        studentRepository.save(student);
    }

    @LogSpendTime
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
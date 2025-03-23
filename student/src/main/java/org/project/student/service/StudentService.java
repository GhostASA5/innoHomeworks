package org.project.student.service;

import lombok.RequiredArgsConstructor;
import org.project.student.aop.LogSpendTime;
import org.project.student.dto.RegisterStudent;
import org.project.student.exception.StudentNotFoundException;
import org.project.student.model.Role;
import org.project.student.model.Student;
import org.project.student.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;

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

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public List<Student> getStudentsOlderThan(Integer age) {
        LocalDate cutoffDate = LocalDate.now().minusYears(age);
        return studentRepository.findStudentsOlderThan(cutoffDate);
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
    public void createStudent(RegisterStudent studentReg) {
        Student student = new Student();
        student.setEmail(studentReg.getEmail());
        student.setFio(studentReg.getFio());
        student.setBirthDay(studentReg.getBirthDay());
        Role role = Role.from(studentReg.getRoleType());
        student.setRoles(Collections.singletonList(role));
        student.setPassword(passwordEncoder.encode(studentReg.getPassword()));

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
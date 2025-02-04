package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.exception.StudentNotFoundException;
import org.project.model.Student;
import org.project.repository.StudentRepository;
import org.springframework.stereotype.Service;

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
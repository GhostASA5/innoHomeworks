package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.exception.StudentNotFoundException;
import org.project.model.Student;
import org.project.repository.StudentRepository;
import org.project.service.StudentService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@DisplayName("Tests for StudentService")
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setEmail("ex1@mail.ru");
        Student student2 = new Student();
        student2.setId(2L);
        student2.setEmail("ex2@mail.ru");

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<Student> students = studentService.getAllStudents();
        assertEquals(2, students.size());
    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student foundStudent = studentService.getStudentById(1L);
        assertNotNull(foundStudent);
        assertEquals(1L, foundStudent.getId());
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        student.setEmail("ex1@mail.ru");

        studentService.createStudent(student);

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testUpdateStudent() {
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setEmail("ex1@mail.ru");

        Student updatedStudent = new Student();
        updatedStudent.setEmail("ex2@mail.ru");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        Student result = studentService.updateStudent(1L, updatedStudent);

        assertNotNull(result);
        assertEquals("ex2@mail.ru", result.getEmail());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void testDeleteStudent() {
        Student existingStudent = new Student();
        existingStudent.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        doNothing().when(studentRepository).deleteById(1L);

        assertDoesNotThrow(() -> studentService.deleteNote(1L));
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudent_StudentFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteNote(1L));
    }
}

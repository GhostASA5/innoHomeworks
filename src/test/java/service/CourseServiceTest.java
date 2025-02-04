package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.model.Course;
import org.project.model.Student;
import org.project.repository.CourseRepository;
import org.project.service.CourseService;
import org.project.service.StudentService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@DisplayName("Tests for CourseService")
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);

        course = new Course();
        course.setId(1L);
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourses() {
        List<Course> courses = Arrays.asList(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void addCourse_ShouldSaveCourse() {
        courseService.addCourse(course);

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void registerStudentOnCourse_ShouldAssignStudentToCourseAndSave() {
        when(studentService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.registerStudentOnCourse(1L, 1L);

        assertEquals(student, course.getStudent());
        verify(courseRepository, times(1)).save(course);
    }
}

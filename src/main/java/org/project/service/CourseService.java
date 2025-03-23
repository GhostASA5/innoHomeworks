package org.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exception.CourseNotFoundException;
import org.project.model.Course;
import org.project.model.CourseToStudent;
import org.project.repository.CourseRepository;
import org.project.repository.CourseToStudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    private final CourseToStudentRepository courseToStudentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getStudentCourses(long studentId) {
        List<CourseToStudent> courseToStudents = courseToStudentRepository.findByStudentId(studentId);
        return courseToStudents.stream()
                .map(CourseToStudent::getCourseId)
                .map(this::getCourseById)
                .toList();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() ->
                new CourseNotFoundException("Course not found"));
    }

    public List<Long> getStudentsWith2AndMoreCourses() {
        return courseToStudentRepository.findStudentsWithMultipleCourses();
    }

    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public void archive(Long id) {
        Course existingCourse = getCourseById(id);
        existingCourse.setActiveStatus(true);
        courseRepository.save(existingCourse);
    }

    public void updateCourse(Long id, Course course) {
        Course existingCourse = getCourseById(id);
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setStartDate(course.getStartDate());
        courseRepository.save(existingCourse);
    }

    public void changeStatus(Long id, Boolean status) {
        Course existingCourse = getCourseById(id);
        existingCourse.setActiveStatus(status);
        courseRepository.save(existingCourse);
    }
}

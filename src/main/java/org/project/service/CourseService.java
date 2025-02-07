package org.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exception.CourseNotFoundException;
import org.project.model.Course;
import org.project.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() ->
                new CourseNotFoundException("Course not found"));
    }

    public void addCourse(Course course) {
        courseRepository.save(course);
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

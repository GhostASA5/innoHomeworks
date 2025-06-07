package org.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exception.CourseNotFoundException;
import org.project.model.ArchivedCourse;
import org.project.model.Course;
import org.project.repository.ArchivedCourseRepository;
import org.project.repository.CourseRepository;
import org.project.repository.CourseToStudentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    private final ArchivedCourseRepository archivedCourseRepository;

    private final CourseToStudentRepository courseToStudentRepository;

    @Cacheable
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Cacheable(key = "#id")
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() ->
                new CourseNotFoundException("Course not found"));
    }

    public List<Long> getStudentsWith2AndMoreCourses() {
        return courseToStudentRepository.findStudentsWithMultipleCourses();
    }

    @CacheEvict(allEntries = true)
    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    @CacheEvict(allEntries = true)
    public void archive(Long id) {
        Course existingCourse = getCourseById(id);
        existingCourse.setActiveStatus(true);
        courseRepository.save(existingCourse);
    }

    @CacheEvict(key = "#id")
    public void updateCourse(Long id, Course course) {
        Course existingCourse = getCourseById(id);
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setStartDate(course.getStartDate());
        courseRepository.save(existingCourse);
    }

    @CacheEvict(key = "#id")
    public void changeStatus(Long id) {
        Course existingCourse = getCourseById(id);
        ArchivedCourse archivedCourse = new ArchivedCourse();
        archivedCourse.setCourseName(existingCourse.getCourseName());
        archivedCourse.setStartDate(existingCourse.getStartDate());

        archivedCourseRepository.save(archivedCourse);
        courseRepository.delete(existingCourse);
    }
}

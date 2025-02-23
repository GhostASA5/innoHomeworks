package org.project.repository;

import org.project.model.CourseToStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseToStudentRepository extends JpaRepository<CourseToStudent, Long> {

    @Query("SELECT c.studentId FROM CourseToStudent c GROUP BY c.studentId HAVING COUNT(c.studentId) > 1")
    List<Long> findStudentsWithMultipleCourses();
}

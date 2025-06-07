package org.project.student.repository;

import org.project.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByCourseId(Long courseId);

    @Query("SELECT s FROM Student s WHERE s.birthDay < :cutoffDate")
    List<Student> findStudentsOlderThan(@Param("cutoffDate") LocalDate cutoffDate);

    List<Student> findByCountry(String country);
}
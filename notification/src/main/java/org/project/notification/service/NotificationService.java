package org.project.notification.service;

import lombok.AllArgsConstructor;
import org.project.notification.client.CourseServiceClient;
import org.project.notification.client.StudentsServiceClient;
import org.project.notification.model.Course;
import org.project.notification.model.Student;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private CourseServiceClient courseServiceClient;

    private StudentsServiceClient studentsServiceClient;

    private JavaMailSender mailSender;

    public void checkAndNotifyStudents() {
        List<Course> courses = courseServiceClient.getAllCourses();
        LocalDate now = LocalDate.now();

        for (Course course : courses) {
            if (course.getStartDate().isEqual(now)) {
                List<Student> students = studentsServiceClient.getStudentsByCourseId(course.getId());
                for (Student student : students) {
                    sendNotification(student.getEmail(), course);
                }
            } else if (course.getStartDate().isBefore(now)) {
                courseServiceClient.archiveCourse(course.getId());
            }
        }
    }

    private void sendNotification(String email, Course course) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Уведомление о начале курса");
        message.setText("Курс " + course.getCourseName() + " начинается сегодня!");
        mailSender.send(message);
    }
}

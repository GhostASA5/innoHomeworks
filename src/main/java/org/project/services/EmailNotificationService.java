package org.project.services;


import lombok.RequiredArgsConstructor;
import org.project.model.Student;
import org.project.repository.StudentRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    private final StudentRepository studentRepository;
    private final JavaMailSender mailSender;

    @Async("taskExecutor")
    public CompletableFuture<Void> sendNotificationToAllStudents(String message) {
        List<Student> students = studentRepository.findAll();

        students.forEach(student -> {
            try {
                sendEmail(student.getEmail(), "Notification from University", message);
            } catch (Exception e) {
                // Логирование ошибки
                System.err.println("Failed to send email to " + student.getEmail());
            }
        });

        return CompletableFuture.completedFuture(null);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

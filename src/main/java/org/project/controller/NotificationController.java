package org.project.controller;

import lombok.RequiredArgsConstructor;
import org.project.services.EmailNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailNotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotifications(@RequestBody String message) {
        notificationService.sendNotificationToAllStudents(message);
        return ResponseEntity.accepted().body("Notification process started");
    }
}

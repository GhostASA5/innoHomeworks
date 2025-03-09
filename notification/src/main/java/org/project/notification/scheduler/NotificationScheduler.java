package org.project.notification.scheduler;

import lombok.AllArgsConstructor;
import org.project.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationScheduler {

    private NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduledNotificationCheck() {
        notificationService.checkAndNotifyStudents();
    }
}

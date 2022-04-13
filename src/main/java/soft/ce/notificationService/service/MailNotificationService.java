package soft.ce.notificationService.service;

import soft.ce.notificationService.dto.NotificationByMail;

public interface MailNotificationService {
    void sendMessage(NotificationByMail notificationByMail);
}

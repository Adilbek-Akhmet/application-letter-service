package soft.ce.notificationService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import soft.ce.notificationService.config.MailConfig;
import soft.ce.notificationService.dto.NotificationByMail;
import soft.ce.notificationService.service.MailNotificationService;

@Service
@RequiredArgsConstructor
public class MailNotificationServiceImpl implements MailNotificationService {

    private final JavaMailSender javaMailSender;
    private final MailConfig mailConfig;

    @Async
    @Override
    public void sendMessage(NotificationByMail notificationByMail) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailConfig.getUsername());
            messageHelper.setTo(notificationByMail.getRecipient());
            messageHelper.setSubject(notificationByMail.getSubject());
            messageHelper.setText(notificationByMail.getText());
        };

        sendToEmail(mimeMessagePreparator);
    }

    private void sendToEmail(MimeMessagePreparator mimeMessagePreparator) {
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailSendException e) {
            throw new MailSendException("Mail Exception occurred when sending message");
        }
    }
}

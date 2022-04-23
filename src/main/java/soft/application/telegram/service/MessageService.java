package soft.application.telegram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Service
public record MessageService(MessageSource messageSource) {
    private static final String LOCALE_TAG = "ru-RU";

    public SendMessage getReplyMessage(String chatId, String replyMessage) {
        return new SendMessage(chatId, getMessage(replyMessage));
    }

    private String getMessage(String message) {
        return messageSource.getMessage(message, null, Locale.forLanguageTag(LOCALE_TAG));
    }
}

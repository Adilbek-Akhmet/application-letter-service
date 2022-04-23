package soft.application.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.application.telegram.cache.DataCache;
import soft.application.telegram.config.BotConfig;
import soft.application.telegram.config.ReplyConfig;
import soft.application.telegram.dto.BotState;
import soft.application.web.dto.ApplicationDto;
import soft.application.web.dto.ApplicationStatus;
import soft.application.web.service.ApplicationService;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Objects;

import static soft.application.telegram.dto.BotState.*;

@Service
public record InputMessageHandler(MessageSource messageSource, ReplyConfig replyConfig, BotConfig botConfig, ApplicationService applicationService) {

    private static final String localeTage = "ru-RU";
    public SendMessage handle(BotState botState, Message message) {
        String userAnswer = message.getText();
        Long userId = message.getFrom().getId();
        String chatId = message.getChatId().toString();

        ApplicationDto application = DataCache.getApplication(userId);
        if (ASK_FULL_NAME.equals(botState)) {
            DataCache.setUserCurrentBotState(userId, ASK_GROUP_NAME);
            return new SendMessage(chatId, replyConfig.getFullName());
        } else if (ASK_GROUP_NAME.equals(botState)) {
            application.setFullName(userAnswer);
            DataCache.setUserCurrentBotState(userId, ASK_PHONE_NUMBER);
            return new SendMessage(chatId, replyConfig.getGroupName());
        } else if (ASK_PHONE_NUMBER.equals(botState)) {
            application.setGroupName(userAnswer);
            DataCache.setUserCurrentBotState(userId, WRITE_APPLICATION);
            return new SendMessage(chatId, replyConfig.getPhoneNumber());
        } else if (WRITE_APPLICATION.equals(botState)) {
            application.setPhoneNumber(userAnswer);
            DataCache.setUserCurrentBotState(userId, CONFIRM_BY_FILE);
            return new SendMessage(chatId, replyConfig.getWriteApplication());
        } else if (CONFIRM_BY_FILE.equals(botState)) {
            application.setApplicationText(userAnswer);
            if (!message.hasDocument()) {
                return new SendMessage(chatId, replyConfig.getConfirmByFile());
            }
            DataCache.setUserCurrentBotState(userId, FINISH);
            return new SendMessage(chatId, replyConfig.getConfirmByFile());
        } else if (FINISH.equals(botState)) {
            String documentTelegramFileUrl = getDocumentTelegramFileUrl(message.getDocument().getFileId());
            application.setConfirmationFilePath(documentTelegramFileUrl);
            application.setTelegramUsername(message.getFrom().getUserName());
            application.setTelegramChatId(chatId);
            application.setCreatedAt(LocalDateTime.now());
            application.setApplicationStatus(ApplicationStatus.IN_PROGRESS);
            return new SendMessage(chatId, replyConfig.getFinish());
        } else {
            throw new IllegalStateException("No such bot state: " + botState);
        }

    }

    private String getDocumentTelegramFileUrl(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<ApiResponse<File>> response = restTemplate.exchange(
                    MessageFormat.format("{0}bot{1}/getFile?file_id={2}", "https://api.telegram.org/", botConfig.getBotToken(), fileId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<File>>() {
                    }
            );
            return Objects.requireNonNull(response.getBody()).getResult().getFileUrl(botConfig.getBotToken());
        } catch (Exception e) {
            throw new IllegalStateException("Telegram file exception");
        }
    }

//    private String getMessage(String message) {
//        return messageSource.getMessage(message, null, Loca);
//    }
}

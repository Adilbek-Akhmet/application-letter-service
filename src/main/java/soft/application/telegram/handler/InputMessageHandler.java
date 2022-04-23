package soft.application.telegram.handler;

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
import soft.application.telegram.dto.BotState;
import soft.application.telegram.service.MessageService;
import soft.application.web.dto.ApplicationDto;
import soft.application.web.dto.ApplicationStatus;
import soft.application.web.service.ApplicationService;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Objects;

import static soft.application.telegram.dto.BotState.*;

@Service
public record InputMessageHandler(
        BotConfig botConfig,
        ApplicationService applicationService,
        MessageService messageService
) {


    public SendMessage handle(BotState botState, Message message) {
        String userAnswer = message.getText();
        Long userId = message.getFrom().getId();
        String chatId = message.getChatId().toString();

        ApplicationDto application = DataCache.getApplication(userId);
        if (ASK_FULL_NAME.equals(botState)) {
            DataCache.setUserCurrentBotState(userId, ASK_GROUP_NAME);
            return messageService.getReplyMessage(chatId, "reply.fullName");
        } else if (ASK_GROUP_NAME.equals(botState)) {
            application.setFullName(userAnswer);
            DataCache.setUserCurrentBotState(userId, ASK_PHONE_NUMBER);
            return messageService.getReplyMessage(chatId, "reply.groupName");
        } else if (ASK_PHONE_NUMBER.equals(botState)) {
            application.setGroupName(userAnswer);
            DataCache.setUserCurrentBotState(userId, WRITE_APPLICATION);
            return messageService.getReplyMessage(chatId, "reply.phoneNumber");
        } else if (WRITE_APPLICATION.equals(botState)) {
            application.setPhoneNumber(userAnswer);
            DataCache.setUserCurrentBotState(userId, CONFIRM_BY_FILE);
            return messageService.getReplyMessage(chatId, "reply.writeApplication");
        } else if (CONFIRM_BY_FILE.equals(botState)) {
            application.setApplicationText(userAnswer);
            if (!message.hasDocument()) {
                return messageService.getReplyMessage(chatId, "reply.confirmByFile");
            }
            DataCache.setUserCurrentBotState(userId, FINISH);
            return messageService.getReplyMessage(chatId, "reply.confirmByFile");
        } else if (FINISH.equals(botState)) {
            String documentTelegramFileUrl = getDocumentTelegramFileUrl(message.getDocument().getFileId());
            application.setConfirmationFilePath(documentTelegramFileUrl);
            application.setTelegramUsername(message.getFrom().getUserName());
            application.setTelegramChatId(chatId);
            application.setCreatedAt(LocalDateTime.now());
            application.setApplicationStatus(ApplicationStatus.IN_PROGRESS);
            return messageService.getReplyMessage(chatId, "reply.finish");
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

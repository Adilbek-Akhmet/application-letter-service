package soft.ce.telegram.handlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.accountService.dto.UserDto;
import soft.ce.accountService.service.UserService;
import soft.ce.applicationService.dto.ApplicationDto;
import soft.ce.applicationService.dto.ApplicationStatus;
import soft.ce.applicationService.dto.ApplicationType;
import soft.ce.applicationService.service.ApplicationService;
import soft.ce.telegram.cache.DataCache;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.handlers.InputMessageHandler;
import soft.ce.telegram.services.ReplyMessageService;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApplicationHandler implements InputMessageHandler {

    private final ReplyMessageService replyMessageService;
    private final DataCache userDataCache;
    private final UserService userService;

    @Override
    public SendMessage handle(Message message) {
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.APPLICATION;
    }

    private SendMessage processUserInput(Message message) {
        String userAnswer = message.getText();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        UserDto userDto = userDataCache.getUser(userId);

        UserDto user = userService.findByEmail(userDto.getEmail());

        ApplicationDto applicationDto = ApplicationDto.builder()
                .applicationText(userAnswer)
                .applicationType(userDataCache.getUserCurrentBotState(userId).name())
                .applicationStatus(ApplicationStatus.IN_PROGRESS)
                .user(user)
                .createdAt(LocalDateTime.now().plusHours(6))
                .build();

        userDataCache.setApplication(userId, applicationDto);

        userDataCache.setUserCurrentBotState(userId, BotState.CONFIRMATION_FILE);

        return replyMessageService.getReplyMessage(chatId, "reply.confirmationFile");
    }



}

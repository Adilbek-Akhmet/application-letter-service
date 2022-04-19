package soft.ce.telegram.handlers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.accountService.dto.UserDto;
import soft.ce.accountService.service.UserService;
import soft.ce.authService.service.ConfirmationTokenService;
import soft.ce.telegram.cache.DataCache;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.handlers.InputMessageHandler;
import soft.ce.telegram.keyboards.Buttons;
import soft.ce.telegram.services.ReplyMessageService;

@Service
@RequiredArgsConstructor
public class UserInfoHandler implements InputMessageHandler {

    private final DataCache userDataCache;
    private final ReplyMessageService replyMessageService;

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId()).equals(BotState.USER_INFO)) {
            userDataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.USERNAME);
        }
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.USER_INFO;
    }

    private SendMessage processUserInput(Message message) {
        String userAnswer = message.getText();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        UserDto user = userDataCache.getUser(userId);
        BotState botState = userDataCache.getUserCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.USERNAME)) {
            userDataCache.setUserCurrentBotState(userId, BotState.RECORD_USERNAME);
            return replyMessageService.getReplyMessage(message.getChatId(), "reply.userName");
        } else if (botState.equals(BotState.RECORD_USERNAME)) {
            user.setFullName(userAnswer);
            userDataCache.setUserCurrentBotState(userId, BotState.GROUP_NAME);
        }

        if (botState.equals(BotState.GROUP_NAME)) {
            userDataCache.setUserCurrentBotState(userId, BotState.RECORD_GROUP_NAME);
            return replyMessageService.getReplyMessage(message.getChatId(), "reply.groupName");
        } else if (botState.equals(BotState.RECORD_GROUP_NAME)) {
            user.setGroupName(userAnswer);
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.pickComplaintType");
            replyToUser.setReplyMarkup(Buttons.getComplaintTypePickButtons());
            userDataCache.setUserCurrentBotState(userId, BotState.APPLICATION);
        }

        userDataCache.setUser(userId, user);

        return replyToUser;
    }
}

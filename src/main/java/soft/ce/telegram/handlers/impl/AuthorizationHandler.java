package soft.ce.telegram.handlers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.accountService.dto.UserDto;
import soft.ce.accountService.service.UserService;
import soft.ce.authService.model.ConfirmationToken;
import soft.ce.authService.service.ConfirmationTokenService;
import soft.ce.telegram.cache.DataCache;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.handlers.InputMessageHandler;
import soft.ce.telegram.keyboards.Buttons;
import soft.ce.telegram.services.ReplyMessageService;

@Service
@RequiredArgsConstructor
public class AuthorizationHandler implements InputMessageHandler {

    private final DataCache userDataCache;
    private final ConfirmationTokenService confirmationTokenService;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId()).equals(BotState.AUTHORIZATION)) {
            userDataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.WRITE_EMAIL);
        }
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.AUTHORIZATION;
    }

    private SendMessage processUserInput(Message message) {
        String userAnswer = message.getText();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        UserDto user = userDataCache.getUser(userId);
        BotState botState = userDataCache.getUserCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.WRITE_EMAIL)) {
            if(userAnswer.endsWith("@gmail.com") || userAnswer.endsWith("@astanait.edu.kz")) {
                if (userService.existsByEmail(userAnswer)) {
                    replyToUser = replyMessageService.getReplyMessage(chatId, "reply.confirmEmail");
                    user.setEmail(userAnswer);
                    confirmationTokenService.sendToken(userAnswer);
                    userDataCache.setUserCurrentBotState(userId, BotState.CONFIRM_EMAIL);
                } else {
                    replyToUser = replyMessageService.getReplyMessage(chatId,"reply.incorrectEmail");
                }
            } else {
                replyToUser = replyMessageService.getReplyMessage(chatId,"reply.incorrectEmailFormat");
            }
        }

        if (botState.equals(BotState.CONFIRM_EMAIL)) {

            ConfirmationToken confirmationToken = confirmationTokenService.findByEmail(user.getEmail());

            if (confirmationToken.getToken().equals(userAnswer)) {
                replyToUser = replyMessageService.getReplyMessage(chatId, "reply.pickComplaintType");
                replyToUser.setReplyMarkup(Buttons.getComplaintTypePickButtons());
                userDataCache.setUserCurrentBotState(userId, BotState.APPLICATION);
            } else {
                replyToUser = replyMessageService.getReplyMessage(chatId, "reply.incorrect_email_code");
            }

        }

        userDataCache.setUser(userId, user);

        return replyToUser;
    }
}

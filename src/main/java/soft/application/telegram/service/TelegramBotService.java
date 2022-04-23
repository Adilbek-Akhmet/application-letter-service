package soft.application.telegram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import soft.application.telegram.cache.DataCache;
import soft.application.telegram.dto.BotState;
import soft.application.telegram.handler.InputMessageHandler;

@Log4j2
@Service
public record TelegramBotService(InputMessageHandler inputMessageHandler) {

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() || message.hasDocument()) {
                log.info("New message from User: {}, chatId: {}, with text: {}",
                        message.getFrom().getUserName(), message.getChatId(), message.getText());
                replyMessage = handleInputMessage(message);
            }
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        Long userId = message.getFrom().getId();
        BotState botState;

        if ("/start".equals(inputMessage)) {
            botState = BotState.ASK_FULL_NAME;
            DataCache.setUserCurrentBotState(userId, botState);
        } else {
            botState = DataCache.getUserCurrentBotState(userId);
        }

        return inputMessageHandler.handle(botState, message);
    }
}


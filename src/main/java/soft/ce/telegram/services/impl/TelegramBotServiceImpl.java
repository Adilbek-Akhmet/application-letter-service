package soft.ce.telegram.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import soft.ce.telegram.cache.DataCache;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.services.MessageHandlerService;
import soft.ce.telegram.services.ReplyMessageService;
import soft.ce.telegram.services.TelegramBotService;

@Log4j2
@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final MessageHandlerService messageHandlerService;
    private final DataCache userDataCache;
    private final ReplyMessageService replyMessageService;

    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("CALLBACK {}", callbackQuery.getMessage());
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        if (update.hasMessage()) {
            log.info("MESSAGE");
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
        SendMessage replyMessage;

        if ("/start".equals(inputMessage) || "start".equals(inputMessage)) {
            botState = BotState.START;
        } else {
            botState = userDataCache.getUserCurrentBotState(userId);
        }
        userDataCache.setUserCurrentBotState(userId, botState);
        log.info("Bot State: {}", botState);
        replyMessage = messageHandlerService.processInputMessage(botState, message);
        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        BotApiMethod<?> callBackAnswer = null;

        if (userDataCache.getUser(userId).getFullName() == null) {
            Message message = new Message();
            Chat chat = new Chat();
            chat.setId(chatId);
            message.setChat(chat);
            return messageHandlerService.processInputMessage(BotState.START, message) ;
        }

        if (callbackQuery.getData().equals(BotState.APPLICATION_HEALTH.name())){
            userDataCache.setUserCurrentBotState(userId, BotState.APPLICATION_HEALTH);
            callBackAnswer = replyMessageService.getReplyMessage(chatId, "reply.writeApplication");
        } else if(callbackQuery.getData().equals(BotState.APPLICATION_PASS_EXAM.name())) {
            userDataCache.setUserCurrentBotState(userId, BotState.APPLICATION_PASS_EXAM);
            callBackAnswer = replyMessageService.getReplyMessage(chatId, "reply.writeApplication");
        } else if(callbackQuery.getData().equals(BotState.APPLICATION_OTHER.name())) {
            userDataCache.setUserCurrentBotState(userId, BotState.APPLICATION_OTHER);
            callBackAnswer = replyMessageService.getReplyMessage(chatId, "reply.writeApplication");
        }
        return callBackAnswer;
    }
}

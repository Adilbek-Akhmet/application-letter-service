package soft.ce.telegram.handlers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.handlers.InputMessageHandler;
import soft.ce.telegram.keyboards.Buttons;
import soft.ce.telegram.services.ReplyMessageService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StartHandler implements InputMessageHandler {

    private final ReplyMessageService replyMessageService;

    @Override
    public SendMessage handle(Message message) {
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START;
    }

    private SendMessage processUserInput(Message message) {
        Long chatId = message.getChatId();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(
                InlineKeyboardButton.builder()
                        .text("Авторизация")
                        .callbackData(BotState.AUTHORIZATION.name())
                        .build()
        );
        SendMessage replyMessage = replyMessageService.getReplyMessage(chatId, "reply.start");
        replyMessage.setReplyMarkup(
                InlineKeyboardMarkup.builder()
                        .keyboardRow(buttons)
                        .build()
        );
        return replyMessage;
    }
}

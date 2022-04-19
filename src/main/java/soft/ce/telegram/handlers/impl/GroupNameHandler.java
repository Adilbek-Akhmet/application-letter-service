package soft.ce.telegram.handlers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.handlers.InputMessageHandler;
import soft.ce.telegram.services.ReplyMessageService;

@Service
@RequiredArgsConstructor
public class GroupNameHandler implements InputMessageHandler {
    private final ReplyMessageService replyMessageService;

    @Override
    public SendMessage handle(Message message) {
        return processUserInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.USERNAME;
    }

    private SendMessage processUserInput(Message message) {
        return replyMessageService.getReplyMessage(message.getChatId(), "reply.groupName");
    }
}

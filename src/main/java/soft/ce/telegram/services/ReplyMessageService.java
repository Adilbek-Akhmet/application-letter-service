package soft.ce.telegram.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ReplyMessageService {
    SendMessage getReplyMessage(Long chatId, String replyMessage);
    SendMessage getReplyMessage(Long chatId, String replyMessage, Object...args);
}

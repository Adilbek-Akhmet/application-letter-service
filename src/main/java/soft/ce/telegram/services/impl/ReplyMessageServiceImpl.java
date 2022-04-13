package soft.ce.telegram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import soft.ce.telegram.services.LocaleMessageService;
import soft.ce.telegram.services.ReplyMessageService;

@Service
@RequiredArgsConstructor
public class ReplyMessageServiceImpl implements ReplyMessageService {

    private final LocaleMessageService localeMessageService;

    @Override
    public SendMessage getReplyMessage(Long chatId, String replyMessage) {
        return new SendMessage(chatId.toString(), localeMessageService.getMessage(replyMessage));
    }

    @Override
    public SendMessage getReplyMessage(Long chatId, String replyMessage, Object... args) {
        return new SendMessage(chatId.toString(), localeMessageService.getMessage(replyMessage, args));
    }
}

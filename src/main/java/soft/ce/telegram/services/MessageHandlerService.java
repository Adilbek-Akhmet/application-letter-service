package soft.ce.telegram.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.telegram.dto.BotState;

public interface MessageHandlerService {
    SendMessage processInputMessage(BotState currentState, Message message);
}

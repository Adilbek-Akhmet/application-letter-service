package soft.ce.telegram.handlers;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.telegram.dto.BotState;

import java.io.IOException;

public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotState getHandlerName();
}

package soft.ce.telegram.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotService {
    BotApiMethod<?> handleUpdate(Update update);
}

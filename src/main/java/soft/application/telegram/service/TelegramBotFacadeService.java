package soft.application.telegram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import soft.application.telegram.config.BotConfig;

@Service
@RequiredArgsConstructor
public class TelegramBotFacadeService extends TelegramWebhookBot {

    private final BotConfig botConfig;
    private final TelegramBotService telegramBotService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return onWebhookUpdateReceived(update);
    }

    @Override
    public String getBotPath() {
        return botConfig.getWebHookPath();
    }
}


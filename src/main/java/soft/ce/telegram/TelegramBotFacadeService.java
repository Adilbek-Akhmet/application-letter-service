package soft.ce.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import soft.ce.telegram.config.BotRegisterConfig;
import soft.ce.telegram.services.TelegramBotService;

@Service
@RequiredArgsConstructor
public class TelegramBotFacadeService extends TelegramWebhookBot {

    private final BotRegisterConfig botConfig;
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
        return telegramBotService.handleUpdate(update);
    }

    @Override
    public String getBotPath() {
        return botConfig.getWebHookPath();
    }
}

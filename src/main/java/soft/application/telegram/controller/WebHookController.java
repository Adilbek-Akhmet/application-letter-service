package soft.application.telegram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import soft.application.telegram.service.TelegramBotFacadeService;

@RestController
@RequiredArgsConstructor
public class WebHookController {

    private final TelegramBotFacadeService telegramBotFacadeService;

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBotFacadeService.onWebhookUpdateReceived(update);
    }
}

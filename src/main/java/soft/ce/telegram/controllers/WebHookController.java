package soft.ce.telegram.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import soft.ce.telegram.TelegramBotFacadeService;

@RestController
@RequiredArgsConstructor
public class WebHookController {

    private final TelegramBotFacadeService telegramBotFacadeService;

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBotFacadeService.onWebhookUpdateReceived(update);
    }
}

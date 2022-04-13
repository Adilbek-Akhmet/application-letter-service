package soft.ce.telegram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import soft.ce.telegram.config.BotMessageConfig;
import soft.ce.telegram.services.LocaleMessageService;

@Service
@RequiredArgsConstructor
public class LocaleMessageServiceImpl implements LocaleMessageService {

    private final BotMessageConfig botMessageConfig;
    private final MessageSource messageSource;

    @Override
    public String getMessage(String message) {
        return messageSource.getMessage(message, null, botMessageConfig.getLocale());
    }

    @Override
    public String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, botMessageConfig.getLocale());
    }
}

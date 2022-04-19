package soft.ce.telegram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.handlers.InputMessageHandler;
import soft.ce.telegram.services.MessageHandlerService;

import java.util.Map;

import static soft.ce.telegram.dto.BotState.*;

@Service
@RequiredArgsConstructor
public class MessageHandlerServiceImpl implements MessageHandlerService {

    private final Map<BotState, InputMessageHandler> messageHandlerMap;

    @Override
    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler messageHandler = findMessageHandler(currentState);
        return messageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isProcessAuthorizationState(currentState)) {
            return messageHandlerMap.get(USER_INFO);
        }

        if (isProcessApplicationState(currentState)) {
            return messageHandlerMap.get(APPLICATION);
        }

        if (currentState.equals(CONFIRMATION_FILE)) {
            return messageHandlerMap.get(CONFIRMATION_FILE);
        }
        return messageHandlerMap.get(currentState);
    }

    private boolean isProcessApplicationState(BotState currentState) {
        return switch (currentState) {
            case APPLICATION_PASS_EXAM, APPLICATION_OTHER, APPLICATION_HEALTH, APPLICATION -> true;
            default -> false;
        };
    }

    private boolean isProcessAuthorizationState(BotState currentState) {
        return switch (currentState) {
            case USERNAME, GROUP_NAME -> true;
            default -> false;
        };
    }
}

package soft.application.telegram.cache;

import soft.application.telegram.dto.BotState;
import soft.application.web.dto.ApplicationDto;

import java.util.HashMap;
import java.util.Map;

public class DataCache {

    private DataCache() {}

    private static final Map<Long, BotState> usersBotState = new HashMap<>();
    private static final Map<Long, ApplicationDto> applicationsData = new HashMap<>();

    public static BotState getUserCurrentBotState(Long userTelegramId) {
        BotState botState = usersBotState.get(userTelegramId);
        if (botState == null) {
            botState = BotState.ASK_FULL_NAME;
        }
        return botState;
    }

    public static void setUserCurrentBotState(Long userTelegramId, BotState botState) {
        usersBotState.put(userTelegramId, botState);
    }

    public static ApplicationDto getApplication(Long userTelegramId) {
        ApplicationDto applicationDto = applicationsData.get(userTelegramId);
        if (applicationDto == null) {
            applicationDto = new ApplicationDto();
        }
        return applicationDto;
    }

    public static void cleanDataByUserTelegramId(Long userTelegramId) {
        applicationsData.remove(userTelegramId);
        usersBotState.remove(userTelegramId);
    }
}

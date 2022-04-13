package soft.ce.telegram.cache;

import soft.ce.accountService.dto.UserDto;
import soft.ce.applicationService.dto.ApplicationDto;
import soft.ce.telegram.dto.BotState;

public interface DataCache {

    BotState getUserCurrentBotState(Long userTelegramId);

    void setUserCurrentBotState(Long userTelegramId, BotState botState);

    UserDto getUser(Long userTelegramId);

    void setUser(Long userTelegramId, UserDto user);

    ApplicationDto getApplication(Long userTelegramId);

    ApplicationDto setApplication(Long userTelegramId, ApplicationDto applicationDto);

    void cleanDataByUserTelegramId(Long userTelegramId);
}

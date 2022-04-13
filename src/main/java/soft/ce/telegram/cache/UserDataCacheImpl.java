package soft.ce.telegram.cache;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import soft.ce.accountService.dto.UserDto;
import soft.ce.applicationService.dto.ApplicationDto;
import soft.ce.telegram.dto.BotState;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class UserDataCacheImpl implements DataCache {

    private final Map<Long, BotState> usersBotState = new HashMap<>();
    private final Map<Long, UserDto> usersData = new HashMap<>();
    private final Map<Long, ApplicationDto> applicationsData = new HashMap<>();

    @Override
    public BotState getUserCurrentBotState(Long userTelegramId) {
        BotState botState = usersBotState.get(userTelegramId);
        if (botState == null || botState.equals(BotState.FINISH)) {
            botState = BotState.START;
        }
        return botState;
    }

    @Override
    public void setUserCurrentBotState(Long userTelegramId, BotState botState) {
        if (botState.equals(BotState.FINISH)) {
            cleanDataByUserTelegramId(userTelegramId);
        } else {
            usersBotState.put(userTelegramId, botState);
        }
    }

    @Override
    public UserDto getUser(Long userTelegramId) {
        UserDto user = usersData.get(userTelegramId);
        if (user == null) {
            user = new UserDto();
        }
        return user;
    }

    @Override
    public void setUser(Long userTelegramId, UserDto user) {
        usersData.put(userTelegramId, user);
    }

    @Override
    public ApplicationDto getApplication(Long userTelegramId) {
        return applicationsData.get(userTelegramId);
    }

    @Override
    public ApplicationDto setApplication(Long userTelegramId, ApplicationDto applicationDto) {
        return applicationsData.put(userTelegramId, applicationDto);
    }

    public void cleanDataByUserTelegramId(Long userTelegramId) {
        usersData.remove(userTelegramId);
        applicationsData.remove(userTelegramId);
        usersBotState.remove(userTelegramId);
    }

//    @Override
//    @Scheduled(cron = "0 12 * * ?")
//    public void cleanCache() {
//        log.info("Cache data before clean userdata size: {}, applicationsData size {}, usersBotState size: {}", usersData.size(), applicationsData.size(), usersBotState.size());
//        usersData.clear();
//        applicationsData.clear();
//        usersBotState.clear();
//        log.info("Cache data after clean userdata size: {}, applicationsData size {}, usersBotState size: {}", usersData.size(), applicationsData.size(), usersBotState.size());
//    }
}

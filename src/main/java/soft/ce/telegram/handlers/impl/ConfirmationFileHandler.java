package soft.ce.telegram.handlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import soft.ce.applicationService.dto.ApplicationDto;
import soft.ce.applicationService.service.ApplicationService;
import soft.ce.telegram.cache.DataCache;
import soft.ce.telegram.config.BotRegisterConfig;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.handlers.InputMessageHandler;
import soft.ce.telegram.services.ReplyMessageService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Log4j2
@Service
@RequiredArgsConstructor
public class ConfirmationFileHandler implements InputMessageHandler {

    private final DataCache userDataCache;
    private final ApplicationService applicationService;
    private final BotRegisterConfig botRegisterConfig;
    private final ReplyMessageService replyMessageService;

    @Override
    public SendMessage handle(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String fileId = message.getDocument().getFileId();

        ApplicationDto application = userDataCache.getApplication(userId);

        URL url = null;
        try {
            url = new URL("https://api.telegram.org/bot" + botRegisterConfig.getBotToken() + "/getFile?file_id=" + fileId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String filePath = "";
//        if (url !=null) {
//            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
//                String result = in.readLine();
//                JSONObject jsonResult = new JSONObject(result);
//                JSONObject path = jsonResult.getJSONObject("result");
//                filePath = path.getString("file_path");
//                application.setConfirmationFilePath(filePath);
//            } catch (IOException | JSONException exception) {
//                exception.printStackTrace();
//            }
//        }

        if (StringUtils.isNotBlank(fileId) && !filePath.isEmpty()) {
            application.setConfirmationFilePath(filePath);
        }

        applicationService.save(application);
        log.info("Application saved with data: {}", application);


        userDataCache.setUserCurrentBotState(userId, BotState.FINISH);

        return replyMessageService.getReplyMessage(chatId, "reply.complaintFinish");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CONFIRMATION_FILE;
    }
}

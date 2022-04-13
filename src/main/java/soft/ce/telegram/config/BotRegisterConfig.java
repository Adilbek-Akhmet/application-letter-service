package soft.ce.telegram.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("telegram.bot")
public class BotRegisterConfig {
    private String webHookPath;
    private String botUsername;
    private String botToken;
}

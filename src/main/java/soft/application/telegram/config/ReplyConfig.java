package soft.application.telegram.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("reply")
public class ReplyConfig {
    private String fullName;
    private String groupName;
    private String phoneNumber;
    private String writeApplication;
    private String confirmByFile;
    private String finish;
}

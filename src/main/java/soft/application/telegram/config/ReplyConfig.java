package soft.application.telegram.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties("reply")
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class ReplyConfig {
    private String fullName;
    private String groupName;
    private String phoneNumber;
    private String writeApplication;
    private String confirmByFile;
    private String finish;
}

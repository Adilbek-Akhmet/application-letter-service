package soft.application.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("security.admin")
public class AdminConfig {
    private String username;
    private String password;
}

package soft.ce.authService;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("admin")
public class Admin {
    private String username;
    private String password;
}

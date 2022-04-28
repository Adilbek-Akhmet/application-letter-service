package soft.application;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import soft.application.telegram.config.BotConfig;

@Log4j2
@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@ConfigurationPropertiesScan
public class ApplicationLetterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationLetterServiceApplication.class, args);
    }

    private final BotConfig botConfig;

    @Autowired
    public ApplicationLetterServiceApplication(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Scheduled(fixedDelay = 300000)
    public void pingMe() {
        log.info("Ping to {}", botConfig.getWebHookPath());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(botConfig.getWebHookPath(), Object.class);
        log.info("Ping was successful {}", botConfig.getWebHookPath());
    }
}

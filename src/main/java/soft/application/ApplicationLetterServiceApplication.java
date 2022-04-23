package soft.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApplicationLetterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationLetterServiceApplication.class, args);
    }

}

package soft.ce.applicationService.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import soft.ce.accountService.dto.UserDto;
import soft.ce.applicationService.dto.ApplicationStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "applications")
public class Application {

    @Id
    private String id;

    private String applicationText;

    private String applicationType;

    private ApplicationStatus applicationStatus;

    private String confirmationFilePath;

    private UserDto user;

    private LocalDateTime createdAt;

    public Application(String applicationText, String applicationType, UserDto user) {
        this.applicationText = applicationText;
        this.applicationType = applicationType;
        this.user = user;
    }
}

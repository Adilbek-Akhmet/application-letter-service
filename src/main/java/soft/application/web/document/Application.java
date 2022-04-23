package soft.application.web.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import soft.application.web.dto.ApplicationStatus;

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
    private ApplicationStatus applicationStatus;
    private String confirmationFilePath;
    private String fullName;
    private String groupName;
    private String phoneNumber;
    private String telegramUsername;
    private String telegramChatId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}

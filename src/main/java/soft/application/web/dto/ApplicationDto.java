package soft.application.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
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

    public String getApplicationTime() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}

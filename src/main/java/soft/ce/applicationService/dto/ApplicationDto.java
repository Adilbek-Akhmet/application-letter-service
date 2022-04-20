package soft.ce.applicationService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import soft.ce.accountService.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
    private String id;
    private String applicationText;
    private String applicationType;
    private ApplicationStatus applicationStatus;
    private String confirmationFilePath;
    private UserDto user;
    public LocalDateTime createdAt;

    public String getApplicationTime() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}

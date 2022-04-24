package soft.application.web.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soft.application.web.dto.ApplicationStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private String text;
    private ApplicationStatus status;
    private String author;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

    public String getCreatedAt() {
        return time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

}

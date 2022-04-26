package soft.application.web.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import soft.application.web.dto.ApplicationStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private String text;
    private ApplicationStatus status;
    private String author;

    private String deadline;

    private String type;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

    public String getDeadlineFormat() {
        LocalDateTime localDateTime = LocalDateTime.parse(deadline);
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();

        String day = localDate.getDayOfMonth() < 10 ? "0" + localDate.getDayOfMonth() : String.valueOf(localDate.getDayOfMonth());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : String.valueOf(localDate.getMonthValue());

        return day + "." + month + "." + localDate.getYear() + " "  + localTime;
    }


    public String getCreatedAt() {
        return time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

}

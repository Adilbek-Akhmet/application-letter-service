package soft.ce.authService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import soft.ce.accountService.dto.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "confirmationToken")
public class ConfirmationToken {

    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    private LocalDateTime expireAt;

    private UserDto user;

    public ConfirmationToken(String token, LocalDateTime expireAt, UserDto user) {
        this.token = token;
        this.expireAt = expireAt;
        this.user = user;
    }
}

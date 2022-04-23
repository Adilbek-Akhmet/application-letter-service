package soft.application.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private String id;
    private String email;
    private String password;
    private String newPassword;
    private String retypeNewPassword;
}

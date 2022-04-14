package soft.ce.authService.service;

import soft.ce.accountService.dto.UserDto;
import soft.ce.authService.model.ConfirmationToken;

public interface ConfirmationTokenService {

    void sendToken(String email);

    String generateToken();

    ConfirmationToken findByEmail(String email);

    ConfirmationToken findByToken(String confirmationLinkToken);

//    void deleteByUser(UserDto userDto);

}

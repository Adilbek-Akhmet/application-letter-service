package soft.ce.authService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import soft.ce.accountService.dto.UserDto;
import soft.ce.accountService.service.UserService;
import soft.ce.authService.model.ConfirmationToken;
import soft.ce.authService.repository.ConfirmationTokenRepository;
import soft.ce.authService.service.ConfirmationTokenService;
import soft.ce.notificationService.dto.NotificationByMail;
import soft.ce.notificationService.service.MailNotificationService;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailNotificationService mailNotificationService;
    private final UserService userService;

    @Async
    @Override
    public void sendToken(String email) {
        UserDto userDto = userService.findByEmail(email);

        if (confirmationTokenRepository.existsByUser(userDto)) {
            confirmationTokenRepository.deleteByUser(userDto);
        }

        String token = generateToken();

        mailNotificationService.sendMessage(
                new NotificationByMail(
                        email,
                        "Confirmation Token",
                        token
                ));

        confirmationTokenRepository.save(new ConfirmationToken(
                token, LocalDateTime.now().plusMinutes(2), userDto));

        log.info("Confirmation Token sent");
    }

    @Override
    public String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        int randomInt = secureRandom.nextInt(10_000_000, 99_999_999);
        return String.valueOf(randomInt);
    }

    @Override
    public ConfirmationToken findByEmail(String email) {
        return confirmationTokenRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
    }

    @Override
    public ConfirmationToken findByToken(String confirmationLinkToken) {
        return confirmationTokenRepository.findByToken(confirmationLinkToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
    }

    @Override
    public void deleteByUser(UserDto userDto) {
        confirmationTokenRepository.deleteByUser(userDto);
    }
}

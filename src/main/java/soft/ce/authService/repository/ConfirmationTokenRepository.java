package soft.ce.authService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import soft.ce.accountService.dto.UserDto;
import soft.ce.authService.model.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {

    Optional<ConfirmationToken> findByToken(String token);

    Optional<ConfirmationToken> findByUser_Email(String email);

    boolean existsByUser(UserDto userDto);

    void deleteByUser_Email(String email);
}

package soft.ce.accountService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import soft.ce.accountService.document.User;
import soft.ce.accountService.dto.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByRole(Role role);
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
}

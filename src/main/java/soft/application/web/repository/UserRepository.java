package soft.application.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import soft.application.web.document.User;
import soft.application.web.dto.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByRole(Role role);
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
}

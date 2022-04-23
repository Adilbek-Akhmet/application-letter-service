package soft.application.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import soft.application.web.document.Application;

public interface ApplicationRepository extends MongoRepository<Application, String> {
}

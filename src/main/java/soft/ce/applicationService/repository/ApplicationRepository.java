package soft.ce.applicationService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import soft.ce.applicationService.document.Application;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findAllByApplicationType(String applicationType);
}

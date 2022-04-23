package soft.application.web.service;

import soft.application.web.document.Reply;
import soft.application.web.dto.ApplicationDto;
import soft.application.web.dto.ApplicationStatus;

import java.util.List;

public interface ApplicationService {
    List<ApplicationDto> findAll();
    ApplicationDto findById(String id);
    ApplicationDto save(ApplicationDto applicationDto);
    ApplicationDto saveReply(String id, ApplicationStatus status, Reply reply);
}

package soft.application.web.service;

import soft.application.web.dto.ApplicationDto;

import java.util.List;

public interface ApplicationService {
    List<ApplicationDto> findAll();
    ApplicationDto findById(String id);
    ApplicationDto save(ApplicationDto applicationDto);
}

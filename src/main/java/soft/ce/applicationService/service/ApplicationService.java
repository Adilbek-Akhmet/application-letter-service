package soft.ce.applicationService.service;

import soft.ce.applicationService.dto.ApplicationDto;

import java.util.List;

public interface ApplicationService {
    List<ApplicationDto> findAll();
    ApplicationDto findById(String id);
    List<ApplicationDto> findAllByApplicationType(String type);
    ApplicationDto save(ApplicationDto applicationDto);
}

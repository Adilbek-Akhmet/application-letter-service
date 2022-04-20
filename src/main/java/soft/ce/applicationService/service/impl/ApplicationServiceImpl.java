package soft.ce.applicationService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soft.ce.applicationService.dto.ApplicationDto;
import soft.ce.applicationService.repository.ApplicationRepository;
import soft.ce.applicationService.service.ApplicationService;
import soft.ce.applicationService.utility.ApplicationMapper;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public List<ApplicationDto> findAll() {
        return applicationRepository.findAll().stream()
                .map(ApplicationMapper::toDto)
                .sorted(Comparator.comparing(ApplicationDto::getApplicationTime).reversed())
                .toList();
    }

    @Override
    public ApplicationDto findById(String id) {
        return ApplicationMapper.toDto(applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Application not found with id: " + id)));
    }

    @Override
    public List<ApplicationDto> findAllByApplicationType(String type) {
        return applicationRepository.findAllByApplicationType(type).stream()
                .map(ApplicationMapper::toDto)
                .toList();
    }

    @Override
    public ApplicationDto save(ApplicationDto applicationDto) {
        return ApplicationMapper
                .toDto(applicationRepository.save(ApplicationMapper.toEntity(applicationDto)));
    }
}

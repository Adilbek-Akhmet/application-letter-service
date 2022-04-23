package soft.application.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soft.application.web.dto.ApplicationDto;
import soft.application.web.repository.ApplicationRepository;
import soft.application.web.service.ApplicationService;
import soft.application.web.utility.ApplicationMapper;

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
                .sorted(Comparator.comparing(ApplicationDto::getCreatedAt).reversed())
                .toList();
    }

    @Override
    public ApplicationDto findById(String id) {
        return ApplicationMapper.toDto(applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Application not found with id: " + id)));
    }

    @Override
    public ApplicationDto save(ApplicationDto applicationDto) {
        return ApplicationMapper
                .toDto(applicationRepository.save(ApplicationMapper.toEntity(applicationDto)));
    }
}

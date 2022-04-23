package soft.application.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import soft.application.web.document.Reply;
import soft.application.web.dto.ApplicationDto;
import soft.application.web.dto.ApplicationStatus;
import soft.application.web.repository.ApplicationRepository;
import soft.application.web.service.ApplicationService;
import soft.application.web.utility.ApplicationMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public ApplicationDto saveReply(String id, ApplicationStatus status, Reply reply) {
        ApplicationDto applicationDto = this.findById(id);
        applicationDto.setApplicationStatus(status);

        if (StringUtils.isNotBlank(reply.getText())) {
            List<Reply> replies = applicationDto.getReplies();
            if (replies == null) {
                replies = new ArrayList<>();
            }
            reply.setStatus(status);
            reply.setTime(LocalDateTime.now());
            reply.setText(reply.getText());
            replies.add(reply);
            applicationDto.setReplies(replies);
        }
        return this.save(applicationDto);
    }
}

package soft.ce.applicationService.utility;

import soft.ce.applicationService.document.Application;
import soft.ce.applicationService.dto.ApplicationDto;

public class ApplicationMapper {

    private ApplicationMapper() {
    }

    public static Application toEntity(ApplicationDto dto) {
        return Application.builder()
                .id(dto.getId())
                .applicationText(dto.getApplicationText())
                .applicationType(dto.getApplicationType())
                .applicationStatus(dto.getApplicationStatus())
                .confirmationFilePath(dto.getConfirmationFilePath())
                .user(dto.getUser())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    public static ApplicationDto toDto(Application doc) {
        return ApplicationDto.builder()
                .id(doc.getId())
                .applicationText(doc.getApplicationText())
                .applicationType(doc.getApplicationType())
                .applicationStatus(doc.getApplicationStatus())
                .confirmationFilePath(doc.getConfirmationFilePath())
                .user(doc.getUser())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}

package soft.application.web.utility;

import soft.application.web.document.Application;
import soft.application.web.dto.ApplicationDto;

public class ApplicationMapper {

    private ApplicationMapper() {
    }

    public static Application toEntity(ApplicationDto dto) {
        return Application.builder()
                .id(dto.getId())
                .applicationText(dto.getApplicationText())
                .applicationStatus(dto.getApplicationStatus())
                .confirmationFilePath(dto.getConfirmationFilePath())
                .fullName(dto.getFullName())
                .groupName(dto.getGroupName())
                .phoneNumber(dto.getPhoneNumber())
                .createdAt(dto.getCreatedAt())
                .replies(dto.getReplies())
                .telegramChatId(dto.getTelegramChatId())
                .telegramUsername(dto.getTelegramUsername())
                .build();
    }

    public static ApplicationDto toDto(Application doc) {
        return ApplicationDto.builder()
                .id(doc.getId())
                .applicationText(doc.getApplicationText())
                .applicationStatus(doc.getApplicationStatus())
                .confirmationFilePath(doc.getConfirmationFilePath())
                .fullName(doc.getFullName())
                .groupName(doc.getGroupName())
                .phoneNumber(doc.getPhoneNumber())
                .createdAt(doc.getCreatedAt())
                .replies(doc.getReplies())
                .telegramChatId(doc.getTelegramChatId())
                .telegramUsername(doc.getTelegramUsername())
                .build();
    }
}

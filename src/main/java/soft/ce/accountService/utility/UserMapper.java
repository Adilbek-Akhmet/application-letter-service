package soft.ce.accountService.utility;

import soft.ce.accountService.document.User;
import soft.ce.accountService.dto.UserDto;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .createdAt(dto.getCreatedAt())
                .role(dto.getRole())
                .build();
    }

    public static UserDto toDto(User doc) {
        return UserDto.builder()
                .id(doc.getId())
                .email(doc.getEmail())
                .password(doc.getPassword())
                .createdAt(doc.getCreatedAt())
                .role(doc.getRole())
                .build();
    }
}

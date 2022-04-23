package soft.application.web.service;

import soft.application.web.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    List<UserDto> findTeachers();

    UserDto findById(String id);

    UserDto findByEmail(String email);

    boolean existsByEmail(String email);

    void save(UserDto user);

    void update(String id, UserDto userDto);

    void deleteByEmail(String email);

    void deleteById(String id);
}

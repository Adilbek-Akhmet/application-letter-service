package soft.ce.accountService.service;

import soft.ce.accountService.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    List<UserDto> findAllByRole_Student();

    List<UserDto> findAllByRole_Teacher();

    UserDto findById(String id);

    UserDto findByEmail(String email);

    boolean existsByEmail(String email);

    void save(UserDto user);

    void update(String id, UserDto userDto);

    void deleteByEmail(String email);

    void deleteById(String id);
}

package soft.ce.accountService.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import soft.ce.accountService.dto.Role;
import soft.ce.accountService.dto.UserDto;
import soft.ce.accountService.repository.UserRepository;
import soft.ce.accountService.service.UserService;
import soft.ce.accountService.utility.UserMapper;
import soft.ce.authService.exception.UserWithSuchEmailFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findAllByRole_Student() {
        return userRepository.findAllByRole(Role.STUDENT).stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findAllByRole_Teacher() {
        return userRepository.findAllByRole(Role.TEACHER).stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findById(String id) {
        return UserMapper.toDto(
                userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Username with id " + id + " not found")));
    }


    @Override
    public UserDto findByEmail(String email) {
        return UserMapper.toDto(userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format("Username with email: %s not found", email))));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void save(UserDto userDto) {
        userRepository.save(UserMapper.toEntity(userDto));
    }

    @Override
    public void update(String id, UserDto updatedUserDto) {
        UserDto userDto = findById(id);

        if (StringUtils.isNotBlank(updatedUserDto.getPassword())) {
            userDto.setPassword(updatedUserDto.getPassword());
        }

        userRepository.save(UserMapper.toEntity(userDto));
    }

    @Override
    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}

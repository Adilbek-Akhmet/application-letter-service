package soft.application.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import soft.application.web.dto.Role;
import soft.application.web.dto.UserDto;
import soft.application.web.repository.UserRepository;
import soft.application.web.service.UserService;
import soft.application.web.utility.UserMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findTeachers() {
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
        userDto.setRole(Role.TEACHER);
        userDto.setCreatedAt(LocalDate.now());
        if (StringUtils.isNotBlank(userDto.getPassword())) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
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


package soft.application.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soft.application.web.dto.ChangePasswordRequest;
import soft.application.web.dto.Role;
import soft.application.web.dto.UserDto;
import soft.application.web.service.UserService;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String findTeachers(Model model) {
        model.addAttribute("teachers", userService.findTeachers());
        return "teachers/teachers";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("teacher", new UserDto());
        return "teachers/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("teacher") UserDto teacher, RedirectAttributes redirectAttributes) {
        if (userService.existsByEmail(teacher.getEmail())) {
            redirectAttributes.addFlashAttribute("exception", "User with such email already exists");
            return "redirect:/teachers/create";
        }
        userService.save(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable String id) {
        UserDto userDto = userService.findById(id);
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .build();
        model.addAttribute("changePasswordRequest", changePasswordRequest);
        return "teachers/update";
    }

    @GetMapping("/update")
    public String updateByUsername(Model model, Authentication authentication) {
        UserDto userDto = userService.findByEmail(authentication.getName());
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .build();
        model.addAttribute("changePasswordRequest", changePasswordRequest);
        return "teachers/update";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("changePasswordRequest") ChangePasswordRequest request, @PathVariable String id, RedirectAttributes redirectAttributes, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean contains = authorities.contains(new SimpleGrantedAuthority(Role.TEACHER.name()));

        if (!request.getNewPassword().equals(request.getRetypeNewPassword())) {
            redirectAttributes.addFlashAttribute("exception", "Passwords do NOT match");
            return "redirect:/teachers/update/" + id;
        }
        UserDto teacher = UserDto.builder()
                .id(request.getId())
                .email(request.getEmail())
                .password(request.getNewPassword())
                .build();
        userService.update(id, teacher);
        if (contains) {
            return "redirect:/applications";
        }
        return "redirect:/teachers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        userService.deleteById(id);
        return "redirect:/teachers";
    }
}


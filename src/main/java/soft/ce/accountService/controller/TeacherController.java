package soft.ce.accountService.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soft.ce.accountService.dto.ChangePasswordRequest;
import soft.ce.accountService.dto.Role;
import soft.ce.accountService.dto.UserDto;
import soft.ce.accountService.service.UserService;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("teachers", userService.findAllByRole_Teacher());
        return "teachers/teachers";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("teacher", new UserDto());
        return "teachers/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("teacher") UserDto teacher, RedirectAttributes redirectAttributes) {
        teacher.setRole(Role.TEACHER);
        teacher.setCreatedAt(LocalDate.now());
        if (StringUtils.isNotBlank(teacher.getPassword())) {
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }
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

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("changePasswordRequest") ChangePasswordRequest request, @PathVariable String id, RedirectAttributes redirectAttributes) {
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
        return "redirect:/teachers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        userService.deleteById(id);
        return "redirect:/teachers";
    }
}

package soft.application.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soft.application.web.dto.ChangePasswordRequest;
import soft.application.web.dto.UserDto;
import soft.application.web.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final UserService userService;

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


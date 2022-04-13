package soft.ce.accountService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soft.ce.accountService.dto.Role;
import soft.ce.accountService.dto.UserDto;
import soft.ce.accountService.service.UserService;
import soft.ce.authService.exception.UserWithSuchEmailFoundException;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final UserService userService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("students", userService.findAllByRole_Student());
        return "/students/students";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("student", new UserDto());
        return "/students/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("student") UserDto student, RedirectAttributes redirectAttributes) {
        student.setRole(Role.STUDENT);
        student.setCreatedAt(LocalDate.now());
        if (userService.existsByEmail(student.getEmail())) {
            redirectAttributes.addFlashAttribute("exception", "User with such email already exists");
            return "redirect:/students/create";
        }
        userService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        userService.deleteById(id);
        return "redirect:/students";
    }
}

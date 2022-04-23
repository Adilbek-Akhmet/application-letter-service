package soft.application.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String redirectToApplications() {
        return "redirect:/applications";
    }
}

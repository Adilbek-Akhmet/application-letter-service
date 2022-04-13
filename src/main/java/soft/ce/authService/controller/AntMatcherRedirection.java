package soft.ce.authService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AntMatcherRedirection {

    @GetMapping("/")
    public String redirectToApplications() {
        return "redirect:/applications";
    }
}

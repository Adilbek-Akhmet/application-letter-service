package soft.application.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soft.application.telegram.config.BotConfig;
import soft.application.web.dto.ApplicationDto;
import soft.application.web.dto.ApplicationStatus;
import soft.application.web.service.ApplicationService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final BotConfig botRegisterConfig;

    private static final String REDIRECT_APPLICATION = "redirect:/applications/";
    private static final String SUCCESS = "success";

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("applications", applicationService.findAll());
        model.addAttribute("domain", botRegisterConfig.getWebHookPath());
        return "applications/applications";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable String id) {
        ApplicationDto applicationDto = applicationService.findById(id);
        model.addAttribute("applicationDto", applicationDto);
        model.addAttribute("downloadUrl", applicationDto.getConfirmationFilePath());
        return "applications/application";
    }

    @PostMapping("/{id}/accepted")
    public String accepted(@PathVariable String id, RedirectAttributes redirectAttributes) {
        ApplicationDto applicationDto = applicationService.findById(id);
        applicationDto.setApplicationStatus(ApplicationStatus.ACCEPTED);
        applicationService.save(applicationDto);

        redirectAttributes.addFlashAttribute(SUCCESS, "Application status successfully changed to ACCEPTED");
        return REDIRECT_APPLICATION + applicationDto.getId();
    }

    @PostMapping("/{id}/rejected")
    public String rejected(@PathVariable String id, RedirectAttributes redirectAttributes) {
        ApplicationDto applicationDto = applicationService.findById(id);
        applicationDto.setApplicationStatus(ApplicationStatus.REJECTED);
        applicationService.save(applicationDto);

        redirectAttributes.addFlashAttribute(SUCCESS, "Application status successfully changed to REJECTED");
        return REDIRECT_APPLICATION + applicationDto.getId();
    }

    @PostMapping("/{id}/in-progress")
    public String inProgress(@PathVariable String id, RedirectAttributes redirectAttributes) {
        ApplicationDto applicationDto = applicationService.findById(id);
        applicationDto.setApplicationStatus(ApplicationStatus.IN_PROGRESS);
        applicationService.save(applicationDto);

        redirectAttributes.addFlashAttribute(SUCCESS, "Application status successfully changed to IN PROGRESS");
        return REDIRECT_APPLICATION + applicationDto.getId();
    }
}


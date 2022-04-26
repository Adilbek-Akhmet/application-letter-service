package soft.application.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soft.application.common.LogAround;
import soft.application.telegram.config.BotConfig;
import soft.application.web.document.Reply;
import soft.application.web.dto.ApplicationDto;
import soft.application.web.dto.ApplicationStatus;
import soft.application.web.service.ApplicationService;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final BotConfig botConfig;
    private final RestTemplate restTemplate;

    private static final String REDIRECT_APPLICATION = "redirect:/applications/";
    private static final String SUCCESS = "success";

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("applications", applicationService.findAll());
        model.addAttribute("domain", botConfig.getWebHookPath());
        return "applications/applications";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable String id) {
        ApplicationDto applicationDto = applicationService.findById(id);
        if (applicationDto != null) {
            model.addAttribute("applicationDto", applicationDto);
            if (applicationDto.getReplies() != null) {
                model.addAttribute("replies", applicationDto.getReplies().stream()
                        .sorted(Comparator.comparing(Reply::getTime).reversed())
                        .toList()
                );
            }
            if (applicationDto.getConfirmationFilePath() != null) {
                model.addAttribute("downloadUrl", applicationDto.getConfirmationFilePath());
            }
        }
        model.addAttribute("reply", new Reply());
        return "applications/application";
    }

    @LogAround(logArgs = true)
    @PostMapping("/{id}/send")
    public String send(@PathVariable String id, RedirectAttributes redirectAttributes,
                           @ModelAttribute("reply") Reply reply) {
        String url = "https://api.telegram.org/bot{botToken}/sendMessage?chat_id={chat_id}&text={notification_text}";
        String text = Optional.ofNullable(reply.getText())
                .orElse("");
        ApplicationDto applicationDto = applicationService.findById(id);

        if (reply.getType().equals("accept")) {
            String dateText = "";
            if (StringUtils.isNotBlank(reply.getDeadline())) {
                dateText = "Срок до " + reply.getDeadlineFormat() + ". ";
            }
            String replyFullText = "Заявление одобрено. " + dateText + text;
            restTemplate.getForObject(url, Void.class, botConfig.getBotToken(), applicationDto.getTelegramChatId(), replyFullText);
            reply.setText(replyFullText);
            applicationService.saveReply(id, ApplicationStatus.ACCEPTED, reply);
            redirectAttributes.addFlashAttribute(SUCCESS, "Application status successfully changed to ACCEPTED");
        } else {
            String replyFullText = "Заявление отказано. " + text;
            restTemplate.getForObject(url, Void.class, botConfig.getBotToken(), applicationDto.getTelegramChatId(), replyFullText);
            reply.setText(replyFullText);
            applicationService.saveReply(id, ApplicationStatus.REJECTED, reply);
            redirectAttributes.addFlashAttribute(SUCCESS, "Application status successfully changed to REJECTED");
        }

        return REDIRECT_APPLICATION + id;
    }

    @LogAround(logArgs = true)
    @PostMapping("/{id}/rejected")
    public String rejected(@PathVariable String id, RedirectAttributes redirectAttributes,
                           @ModelAttribute("reply") Reply reply) {
        ApplicationDto applicationDto = applicationService.saveReply(id, ApplicationStatus.REJECTED, reply);
        String url = "https://api.telegram.org/bot{botToken}/sendMessage?chat_id={chat_id}&text={notification_text}";
        restTemplate.getForObject(url, Void.class, botConfig.getBotToken(), applicationDto.getTelegramChatId(), reply.getText());
        redirectAttributes.addFlashAttribute(SUCCESS, "Application status successfully changed to REJECTED");
        return REDIRECT_APPLICATION + id;
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


package com.startups.controller;

import com.startups.controller.main.Main;
import com.startups.model.Applications;
import com.startups.model.enums.ApplicationStatus;
import com.startups.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/applications") // отображение запросов на методы контроллера
public class ApplicationsCont extends Main {
    @GetMapping
    public String applications(Model model) {
        AddAttributes(model);
        List<Applications> applications = new ArrayList<>();

        if (getUser().getRole() == Role.ADMIN) {
            applications.addAll(applicationsRepo.findAllByStatus(ApplicationStatus.WAITING));
            applications.addAll(applicationsRepo.findAllByStatus(ApplicationStatus.CONFIRMED));
        } else {
            applications.addAll(applicationsRepo.findAllByStatusAndOwner_Id(ApplicationStatus.REGISTRATION, getUser().getId()));
            applications.addAll(applicationsRepo.findAllByStatusAndOwner_Id(ApplicationStatus.WAITING, getUser().getId()));
            applications.addAll(applicationsRepo.findAllByStatusAndOwner_Id(ApplicationStatus.CONFIRMED, getUser().getId()));
            applications.addAll(applicationsRepo.findAllByStatusAndOwner_Id(ApplicationStatus.DELIVERED, getUser().getId()));
            applications.addAll(applicationsRepo.findAllByStatusAndOwner_Id(ApplicationStatus.REJECTED, getUser().getId()));
        }

        model.addAttribute("applications", applications);
        return "applications";
    }

    @GetMapping("/{id}/wait")
    public String applicationWait(@PathVariable Long id) {
        Applications application = applicationsRepo.getReferenceById(id);
        application.setStatus(ApplicationStatus.WAITING);
        applicationsRepo.save(application);
        return "redirect:/applications";
    }

    @GetMapping("/{id}/conf")
    public String applicationConf(@PathVariable Long id) {
        Applications application = applicationsRepo.getReferenceById(id);
        application.setStatus(ApplicationStatus.CONFIRMED);
        applicationsRepo.save(application);
        return "redirect:/applications";
    }

    @GetMapping("/{id}/reject")
    public String applicationReject(@PathVariable Long id) {
        Applications application = applicationsRepo.getReferenceById(id);
        application.setStatus(ApplicationStatus.REJECTED);
        applicationsRepo.save(application);
        return "redirect:/applications";
    }

    @GetMapping("/{id}/del")
    public String applicationDel(@PathVariable Long id) {
        Applications application = applicationsRepo.getReferenceById(id);
        application.setStatus(ApplicationStatus.DELIVERED);
        applicationsRepo.save(application);
        return "redirect:/applications";
    }
}

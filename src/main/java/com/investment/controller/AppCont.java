package com.investment.controller;

import com.investment.controller.main.Main;
import com.investment.model.ProjectApp;
import com.investment.model.enums.ProjectAppStatus;
import com.investment.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/apps")
public class AppCont extends Main {
    @GetMapping
    public String apps(Model model) {
        getCurrentUserAndRole(model);

        List<ProjectApp> apps;

        if (getUser().getRole() == Role.USER) {
            apps = getUser().getProjectApps();
        } else {
            apps = projectAppRepo.findAll();
        }

        model.addAttribute("apps", apps);

        return "apps";
    }

    @GetMapping("/{id}/done")
    public String done(@PathVariable Long id) {
        ProjectApp app = projectAppRepo.getReferenceById(id);
        app.setStatus(ProjectAppStatus.DONE);
        app.setAdmin(getUser());
        projectAppRepo.save(app);
        return "redirect:/apps";
    }

    @GetMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        ProjectApp app = projectAppRepo.getReferenceById(id);
        app.setStatus(ProjectAppStatus.REJECT);
        app.setAdmin(getUser());
        projectAppRepo.save(app);
        return "redirect:/apps";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        projectAppRepo.deleteById(id);
        return "redirect:/apps";
    }
}
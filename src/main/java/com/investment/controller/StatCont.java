package com.investment.controller;

import com.investment.controller.main.Main;
import com.investment.model.enums.ProjectAppStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stat")
public class StatCont extends Main {
    @GetMapping
    public String stat(Model model) {
        getCurrentUserAndRole(model);

        ProjectAppStatus[] appStatuses = ProjectAppStatus.values();
        String[] appStatusesNames = new String[appStatuses.length];
        int[] appStatusesValues = new int[appStatuses.length];

        for (int i = 0; i < appStatuses.length; i++) {
            appStatusesNames[i] = appStatuses[i].getName();
            appStatusesValues[i] = projectAppRepo.findAllByStatus(appStatuses[i]).size();
        }

        model.addAttribute("appStatusesNames", appStatusesNames);
        model.addAttribute("appStatusesValues", appStatusesValues);

        return "stat";
    }
}

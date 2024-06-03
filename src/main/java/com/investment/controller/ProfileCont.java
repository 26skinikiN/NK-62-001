package com.investment.controller;

import com.investment.controller.main.Main;
import com.investment.model.AppUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileCont extends Main {
    @GetMapping
    public String profile(Model model) {
        getCurrentUserAndRole(model);
        return "profile";
    }

    @PostMapping("/edit")
    public String profileFio(@RequestParam String fio) {
        AppUser user = getUser();
        user.setFio(fio);
        userRepo.save(user);
        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String password(Model model,@RequestParam String password,@RequestParam String password_repeat) {
        if (!password.equals(password_repeat)) {
            getCurrentUserAndRole(model);
            model.addAttribute("message", "Некорректный ввод паролей!");
            return "profile";
        }

        AppUser user = getUser();
        user.setPassword(password);
        userRepo.save(user);

        return "redirect:/profile";
    }

    @GetMapping("/theme")
    public String theme() {
        AppUser user = getUser();
        user.setTheme(!user.isTheme());
        userRepo.save(user);
        return "redirect:/profile";
    }
}

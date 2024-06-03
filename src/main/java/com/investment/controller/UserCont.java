package com.investment.controller;

import com.investment.controller.main.Main;
import com.investment.model.AppUser;
import com.investment.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserCont extends Main {
    @GetMapping
    public String users(Model model) {
        getCurrentUserAndRole(model);
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("roles", Role.values());
        return "users";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @RequestParam Role role) {
        AppUser user = userRepo.getReferenceById(id);
        user.setRole(role);
        userRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/{id}/enabled")
    public String enabled(@PathVariable Long id) {
        AppUser user = userRepo.getReferenceById(id);
        user.setEnabled(!user.isEnabled());
        userRepo.save(user);
        return "redirect:/users";
    }
}
package com.investment.controller;

import com.investment.controller.main.Main;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexCont extends Main {
    @GetMapping("/index")
    public String index1() {
        return "redirect:/about";
    }

    @GetMapping("/")
    public String index2() {
        return "redirect:/about";
    }

    @GetMapping("/about")
    public String about(Model model) {
        getCurrentUserAndRole(model);
        return "about";
    }

}
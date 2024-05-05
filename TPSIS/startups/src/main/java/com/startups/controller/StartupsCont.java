package com.startups.controller;

import com.startups.controller.main.Main;
import com.startups.model.Applications;
import com.startups.model.Startups;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/startups")
public class StartupsCont extends Main {
    @GetMapping
    public String startups(Model model) {
        AddAttributes(model);
        model.addAttribute("startups", startupsRepo.findAll());
        return "startups";
    }

    @GetMapping("/filter")
    public String startupsFilter(Model model, @RequestParam String name, @RequestParam int sort) { // извлечение
        AddAttributes(model);                                                                       // параметров запроса
        model.addAttribute("name", name);                                              // в методе обработчике
        model.addAttribute("sort", sort);
        List<Startups> startups = startupsRepo.findAllByNameContaining(name);

        switch (sort) {
            case 1 -> startups.sort(Comparator.comparing(Startups::getName));
            case 2 -> {
                startups.sort(Comparator.comparing(Startups::getName));
                Collections.reverse(startups);
            }
            case 3 -> startups.sort(Comparator.comparing(Startups::getPrice));
            case 4 -> {
                startups.sort(Comparator.comparing(Startups::getPrice));
                Collections.reverse(startups);
            }
        }

        model.addAttribute("startups", startups);
        return "startups";
    }


    @GetMapping("/add")
    public String startupAdd(Model model) {
        AddAttributes(model);
        return "startup_add";
    }

    @GetMapping("/{id}/edit")
    public String startupEdit(Model model, @PathVariable Long id) {
        AddAttributes(model);
        model.addAttribute("startup", startupsRepo.getReferenceById(id));
        return "startup_edit";
    }

    @GetMapping("/{id}/delete")
    public String startupDelete(@PathVariable Long id) { // связывание значений из url с параметрами метода
        startupsRepo.deleteById(id);
        return "redirect:/startups";
    }

    @GetMapping("/{id}/buy")
    public String startupBuy(@PathVariable Long id) { // связывание значений из url с параметрами метода
        applicationsRepo.save(new Applications(getUser(), startupsRepo.getReferenceById(id)));
        return "redirect:/startups";
    }

    @PostMapping("/add")
    public String startupAdd(Model model, @RequestParam String name, @RequestParam String description, @RequestParam int cores, @RequestParam float frequencyMain, @RequestParam float frequencyMax, @RequestParam float cacheL2, @RequestParam float cacheL3, @RequestParam int technicalProcess, @RequestParam int tdp, @RequestParam float price, @RequestParam MultipartFile file) {
        String res = "";
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "startups/" + uuidFile + "_" + file.getOriginalFilename();
                    file.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (IOException e) {
                model.addAttribute("message", "Некорректные данные!");
                AddAttributes(model);
                return "startup_add";
            }
        }

        startupsRepo.save(new Startups(name, description, cores, frequencyMain, frequencyMax, cacheL2, cacheL3, technicalProcess, tdp, price, res));

        return "redirect:/startups";
    }

    @PostMapping("/{id}/edit")
    public String startupEdit(Model model, @RequestParam String name, @RequestParam String description, @RequestParam int cores, @RequestParam float frequencyMain, @RequestParam float frequencyMax, @RequestParam float cacheL2, @RequestParam float cacheL3, @RequestParam int technicalProcess, @RequestParam int tdp, @RequestParam float price, @RequestParam MultipartFile file, @PathVariable Long id) {
        Startups startup = startupsRepo.getReferenceById(id);

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String res = "";
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = "startups/" + uuidFile + "_" + file.getOriginalFilename();
                    file.transferTo(new File(uploadImg + "/" + res));
                }
                startup.setPhoto(res);
            } catch (IOException e) {
                model.addAttribute("message", "Некорректные данные!");
                AddAttributes(model);
                model.addAttribute("startup", startupsRepo.getReferenceById(id));
                return "startup_edit";
            }
        }

        startup.set(name, description, cores, frequencyMain, frequencyMax, cacheL2, cacheL3, technicalProcess, tdp, price);

        startupsRepo.save(startup);

        return "redirect:/startups";
    }
}


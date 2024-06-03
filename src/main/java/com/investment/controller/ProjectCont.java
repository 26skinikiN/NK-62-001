package com.investment.controller;

import com.investment.controller.main.Main;
import com.investment.model.Project;
import com.investment.model.ProjectApp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/projects")
public class ProjectCont extends Main {
    @GetMapping
    public String projects(Model model) {
        getCurrentUserAndRole(model);
        model.addAttribute("projects", projectRepo.findAll());
        return "projects";
    }

    @GetMapping("/filter")
    public String filter(Model model, @RequestParam String name, @RequestParam int filter) {
        getCurrentUserAndRole(model);
        model.addAttribute("name", name);
        model.addAttribute("filter", filter);

        List<Project> projects = projectRepo.findAllByNameContaining(name);

        projects.sort(Comparator.comparing(Project::getName));

        if (filter == 2) {
            Collections.reverse(projects);
        }

        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/{id}")
    public String project(Model model, @PathVariable Long id) {
        getCurrentUserAndRole(model);
        model.addAttribute("project", projectRepo.getReferenceById(id));
        return "project";
    }

    @PostMapping("/{id}/app")
    public String app(@RequestParam float sum, @PathVariable Long id) {
        projectAppRepo.save(new ProjectApp(getDate(), sum, projectRepo.getReferenceById(id), getUser()));
        return "redirect:/projects/{id}";
    }

    @GetMapping("/add")
    public String add(Model model) {
        getCurrentUserAndRole(model);
        return "project_add";
    }

    @PostMapping("/add")
    public String add(
            Model model, @RequestParam String name, @RequestParam String founder, @RequestParam String date,
            @RequestParam float need, @RequestParam String description,
            @RequestParam MultipartFile photo
    ) {
        Project project = new Project(name, date, description, founder, need, getUser());

        try {
            if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
                project.setPhoto(saveFile(photo, "projects"));
            }
        } catch (IOException e) {
            model.addAttribute("message", "Некорректные данные!");
            getCurrentUserAndRole(model);
            return "project_add";
        }

        project = projectRepo.save(project);
        return "redirect:/projects/" + project.getId();
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        getCurrentUserAndRole(model);
        model.addAttribute("project", projectRepo.getReferenceById(id));
        return "project_edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(
            Model model, @RequestParam String name, @RequestParam String founder, @RequestParam String date,
            @RequestParam float need, @RequestParam String description,
            @RequestParam MultipartFile photo, @PathVariable Long id
    ) {
        Project project = projectRepo.getReferenceById(id);

        try {
            if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
                project.setPhoto(saveFile(photo, "projects"));
            }
        } catch (IOException e) {
            model.addAttribute("message", "Некорректные данные!");
            getCurrentUserAndRole(model);
            model.addAttribute("project", projectRepo.getReferenceById(id));
            return "project_edit";
        }

        project.set(name, date, description, founder, need);
        projectRepo.save(project);
        return "redirect:/projects/{id}";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        projectRepo.deleteById(id);
        return "redirect:/projects";
    }
}

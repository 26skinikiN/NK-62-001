package com.investment.controller.main;

import com.investment.model.AppUser;
import com.investment.repo.ProjectAppRepo;
import com.investment.repo.ProjectRepo;
import com.investment.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Main {
    @Autowired
    protected UserRepo userRepo;
    @Autowired
    protected ProjectRepo projectRepo;
    @Autowired
    protected ProjectAppRepo projectAppRepo;

    @Value("${upload.img}")
    private String uploadImg;

    public static long ONE_DAY = 1000 * 60 * 60 * 24;

    protected void getCurrentUserAndRole(Model model) {
        model.addAttribute("user", getUser());
        model.addAttribute("role", getRole());
        model.addAttribute("fio", getFio());
        model.addAttribute("theme", getTheme());
    }

    protected AppUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            return userRepo.findByUsername(userDetail.getUsername());
        }
        return null;
    }

    private String getRole() {
        AppUser user = getUser();
        if (user == null) return "NOT";
        return user.getRole().name();
    }

    private String getFio() {
        AppUser user = getUser();
        if (user == null) return "Добро пожаловать";
        return user.getFio();
    }

    private String getTheme() {
        AppUser user = getUser();
        if (user == null) return "light";
        if (user.isTheme()) return "light";
        else return "dark";
    }


    public static String getDate() {
        return LocalDateTime.now().toString().substring(0, 10);
    }

    public static float round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    protected String saveFile(MultipartFile photo, String path) throws IOException {
        if (photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            File uploadDir = new File(uploadImg);
            if (!uploadDir.exists()) uploadDir.mkdir();
            String result = path + "/" + uuidFile + "_" + photo.getOriginalFilename();
            photo.transferTo(new File(uploadImg + "/" + result));
            return result;
        } else throw new IOException();
    }

}
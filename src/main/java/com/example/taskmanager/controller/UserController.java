package com.example.taskmanager.controller;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/make-admin/{userId}")
    public String makeAdmin(@PathVariable Long userId) {
        userService.assignAdminRole(userId);
        return "redirect:/admin/users"; // Перенаправление на страницу со списком пользователей
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Указывает на шаблон login.html
    }
}

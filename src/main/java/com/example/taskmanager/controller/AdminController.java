//package com.example.taskmanager.controller;
//
//import com.example.taskmanager.entity.User;
//import com.example.taskmanager.service.UserService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//public class AdminController {
//    @GetMapping("/users")
//    public String listUsers(Model model) {
//        model.addAttribute("users", UserService.getAllUsers());
//        return "admin-users";
//    }
//
//    @PostMapping("/notify")
//    public String notifyUser(@RequestParam Long userId,
//                             @RequestParam String subject,
//                             @RequestParam String message) {
//        User user = UserService.getUserById(userId);
//        userService.sendEmailToUser(user.getEmail(), subject, message);
//        return "redirect:/admin/users";
//    }
//
//    @PostMapping("/tasks/create")
//    public String createTaskForUser(@ModelAttribute Task task, @RequestParam Long userId) {
//        User user = userService.getUserById(userId);
//        task.setUser(user);
//        taskService.saveTask(task);
//        return "redirect:/admin/tasks";
//    }
//
//}

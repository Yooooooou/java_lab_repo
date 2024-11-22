package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.CategoryService;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CategoryService categoryService;

    public TaskController(TaskService taskService, CategoryService categoryService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listTasks(@AuthenticationPrincipal User user,
                            @RequestParam(required = false) Long category, // Получаем ID категории
                            Model model) {
        List<Task> tasks;
        if (category != null) {
            tasks = taskService.getUserTasksByCategory(user, category); // Фильтрация по категории
        } else {
            tasks = taskService.getUserTasks(user); // Все задачи
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", categoryService.getAllCategories()); // Для фильтрации
        model.addAttribute("selectedCategory", category); // Для сохранения выбора
        return "tasks";
    }

    @GetMapping("/create")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "create-task";
    }

    @PostMapping("/create")
    public String createTask(@AuthenticationPrincipal User user,
                             @Valid @ModelAttribute("task") Task task,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "create-task";
        }
        if (task.getDeadline() != null && task.getDeadline().isBefore(task.getCreatedAt())) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("errorMessage", "Дедлайн не может быть раньше времени создания задачи.");
            return "create-task";
        }

        if (user == null) {
            model.addAttribute("error", "Пользователь не найден.");
            return "create-task";
        }

        task.setUser(user);
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
    @GetMapping("/edit/{id}")
    public String showEditPriorityAndCategoryForm(@PathVariable Long id,
                                                  @AuthenticationPrincipal User user,
                                                  Model model) {
        Task task = taskService.getTaskById(id);

        // Проверяем, что задача принадлежит текущему пользователю
        if (!task.getUser().equals(user)) {
            model.addAttribute("error", "Вы не можете редактировать эту задачу.");
            return "redirect:/tasks";
        }

        model.addAttribute("task", task);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-task-priority-category"; // Убедитесь, что есть соответствующий шаблон
    }

    // Новый метод для обновления приоритета и категории
    @PostMapping("/edit/{id}")
    public String updatePriorityAndCategory(@PathVariable Long id,
                                            @AuthenticationPrincipal User user,
                                            @Valid @ModelAttribute("task") Task updatedTask,
                                            BindingResult result,
                                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit-task-priority-category";
        }

        Task existingTask = taskService.getTaskById(id);

        // Проверяем, что задача принадлежит текущему пользователю
        if (!existingTask.getUser().equals(user)) {
            model.addAttribute("error", "Вы не можете редактировать эту задачу.");
            return "redirect:/tasks";
        }

        // Обновляем только приоритет и категорию
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setCategory(updatedTask.getCategory());

        taskService.saveTask(existingTask);
        return "redirect:/tasks";
    }


}

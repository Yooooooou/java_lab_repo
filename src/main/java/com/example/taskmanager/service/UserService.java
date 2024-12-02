package com.example.taskmanager.service;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void assignAdminRole(User user) {
        user.setRole(User.Role.ADMIN);
        userRepository.save(user);
    }

    public boolean isAdmin(User user) {
        return user.getRole() == User.Role.ADMIN;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void assignAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким ID не найден"));
        user.setRole(User.Role.ADMIN); // Назначаем роль ADMIN
        userRepository.save(user); // Сохраняем изменения
    }



    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

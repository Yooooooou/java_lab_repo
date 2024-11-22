package com.example.taskmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime deadline;

    private String priority;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @PrePersist
    public void setCreationDate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }


    @AssertTrue(message = "Дедлайн не может быть раньше времени создания задачи.")
    public boolean isDeadlineValid() {
        // Проверка, что поля не null
        if (this.createdAt == null || this.deadline == null) {
            return true; // Валидация успешна, если одно из значений отсутствует
        }
        // Проверка дедлайна
        return this.deadline.isAfter(this.createdAt);
    }

}

package com.example.taskmanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DeadlineValidator implements ConstraintValidator<ValidDeadline, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime deadline, ConstraintValidatorContext context) {
        return deadline == null || deadline.isAfter(LocalDateTime.now());
    }
}

package com.sharex.app.module.user.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(
        @NotBlank(message = "name must be provided")
        @Size(min = 3, message = "name must be at least 3 characters")
        String name,

        @NotBlank(message = "email must be provided")
        @Email(message="invalid email format")
        String email
) {}

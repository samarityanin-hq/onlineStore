package main.store.DTOs;

import jakarta.validation.constraints.NotBlank;

public record UserRegistration(
        @NotBlank(message = "Name field cannot be empty")
        String name,
        @NotBlank(message = "Email field cannot be empty")
        String email,
        char[] password) {
}

package main.store.DTOs;

import jakarta.validation.constraints.NotBlank;

public record UserRegistration(
        String name,
        String email,
        char[] password) {
}

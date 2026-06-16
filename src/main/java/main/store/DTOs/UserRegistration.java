package main.store.DTOs;

import jakarta.validation.constraints.NotNull;

public record UserRegistration(@NotNull String name,
                               @NotNull String email,
                               @NotNull char[] password) {
}

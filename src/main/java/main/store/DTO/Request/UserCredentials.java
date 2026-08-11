package main.store.DTO.Request;

import jakarta.validation.constraints.*;

public record UserCredentials(
        @Email(message = "incorrect email format")
        @NotBlank
        String email,
        @NotNull(message = "password field cannot be null")
        @NotEmpty(message = "password field cannot be empty")
        String password
) {
}

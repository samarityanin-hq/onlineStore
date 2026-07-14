package main.store.DTO.DTOin;

import jakarta.validation.constraints.*;

public record UserRegistration(
        @NotBlank(message = "name cannot be empty")
        @Size(min = 3, message = "name should be min 3 symbols")
        String name,

        @Email(message = "incorrect email format")
        @NotBlank
        String email,

        @NotNull(message = "password field cannot be null")
        @NotEmpty(message = "password field cannot be empty")
        @Size(min = 8, message = "password should be min 8 symbols")
        char[] password) {
}

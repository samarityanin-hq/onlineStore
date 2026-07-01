package main.store.DTO.DTOin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistration(
        @NotBlank(message = "name cannot be empty")
        @Size(min = 3, message = "name should be min 3 symbols")
        String name,

        @Email(message = "incorrect email format")
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8, message = "password should be min 8 symbols")
        char[] password) {
}

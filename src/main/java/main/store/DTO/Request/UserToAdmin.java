package main.store.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserToAdmin(
        @Email(message = "incorrect email format")
        @NotBlank
        String email
) {
}

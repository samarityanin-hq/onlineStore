package main.store.DTO.Request;

import jakarta.validation.constraints.*;

public record NewCategory(
        @NotBlank(message = "category title cannot be empty")
        @Size(max = 30, message = "title is to long")
        String categoryName
) {
}

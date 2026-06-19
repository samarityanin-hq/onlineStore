package main.store.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductToAdd(
        @NotBlank(message = "product title cannot be empty")
        String title,
        @Positive(message = "price cant be negative")
        @NotNull(message = "empty field price for product")
        BigDecimal price,
        @Positive(message = "quantity cant be negative")
        @NotNull(message = "empty field quantity for product")
        Integer quantity


) {
}

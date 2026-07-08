package main.store.DTO.DTOin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductToAdd(
        @NotBlank(message = "product title cannot be empty")
        @Size(max = 30, message = "title is to long")
        String title,

        @Positive(message = "price cannot be negative")
        @NotNull(message = "empty field price for product")
        BigDecimal price,

        @Positive(message = "quantity cannot be negative")
        @NotNull(message = "empty field quantity for product")
        Integer quantity,

        @Positive(message = "category id cannot be negative")
        @NotNull
        Long categoryId
) {
}

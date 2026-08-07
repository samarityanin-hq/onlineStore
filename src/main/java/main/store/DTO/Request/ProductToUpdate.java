package main.store.DTO.Request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductToUpdate(
        @Size(max = 30, message = "title is to long")
        String newTitle,
        @Positive(message = "price cannot be negative")
        BigDecimal newPrice,
        @Positive(message = "quantity cannot be negative")
        Integer newQuantity
) {
}

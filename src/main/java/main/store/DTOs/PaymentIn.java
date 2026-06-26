package main.store.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentIn(
        @Positive(message = "payment amount cant be negative")
        @NotNull(message = "empty field amount for payment")
        BigDecimal amount
) {
}

package main.store.DTO.DTOin;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentIn(
        @Positive(message = "payment amount cant be negative")
        @NotNull(message = "empty field amount for payment")
        @DecimalMin(value = "0.01")
        BigDecimal amount
) {
}

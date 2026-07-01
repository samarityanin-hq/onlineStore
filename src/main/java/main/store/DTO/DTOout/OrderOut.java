package main.store.DTO.DTOout;

import main.store.Enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderOut(
        Status status,
        BigDecimal totalPrice,
        Integer totalItems,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
}

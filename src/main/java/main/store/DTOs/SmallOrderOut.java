package main.store.DTOs;

import main.store.Repositories.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SmallOrderOut(
        Status status,
        BigDecimal totalPrice,
        Integer totalItems,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
}

package main.store.DTOs;

import main.store.Repositories.Status;

import java.math.BigDecimal;
import java.util.List;

public record OrderOut(
        String email,
        String name,
        Status status,
        BigDecimal price,
        List<OrderItemOut> orderItems
) {
}

package main.store.DTO.DTOout;

import main.store.Enums.Status;

import java.math.BigDecimal;
import java.util.List;

public record FullOrderOut(
        String email,
        String name,
        Status status,
        BigDecimal price,
        List<OrderItemOut> orderItems
) {
}

package main.store.DTOs;

import java.math.BigDecimal;

public record OrderItemOut(
        String itemTitle,
        Integer quantity,
        BigDecimal priceAtPurchase
        ) {
}

package main.store.DTO.Response;

import java.math.BigDecimal;

public record OrderItemOut(
        String itemTitle,
        Integer quantity,
        BigDecimal priceAtPurchase
        ) {
}

package main.store.DTO.DTOout;

import java.math.BigDecimal;

public record OrderItemOut(
        String itemTitle,
        Integer quantity,
        BigDecimal priceAtPurchase
        ) {
}

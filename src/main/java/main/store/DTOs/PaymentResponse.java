package main.store.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long orderId,
        String userEmail,
        BigDecimal orderCost,
        LocalDateTime payTime
) {
}

package main.store.DTO.Response;

import java.math.BigDecimal;

public record ProductOut(String title,
                         BigDecimal price,
                         int quantity){}

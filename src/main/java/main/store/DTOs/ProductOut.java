package main.store.DTOs;

import java.math.BigDecimal;

public record ProductOut(String title,
                         BigDecimal price,
                         int quantity){}

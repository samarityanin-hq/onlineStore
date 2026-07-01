package main.store.DTO.DTOout;

import java.math.BigDecimal;

public record ProductOut(String title,
                         BigDecimal price,
                         int quantity){}

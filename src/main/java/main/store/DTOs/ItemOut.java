package main.store.DTOs;

import java.math.BigDecimal;

public record ItemOut(String name,
                      Integer quantity,
                      BigDecimal totalPrice
                      ){}

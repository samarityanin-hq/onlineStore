package main.store.DTO.DTOout;

import java.math.BigDecimal;

public record ItemOut(String name,
                      Integer quantity,
                      BigDecimal totalPrice
                      ){}

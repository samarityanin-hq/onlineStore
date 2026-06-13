package DTOs;

import java.math.BigDecimal;

public record ItemOut(String name,
                      BigDecimal price,
                      Integer quantity,
                      BigDecimal totalPrice
                      ){}

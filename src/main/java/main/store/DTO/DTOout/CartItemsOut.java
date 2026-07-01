package main.store.DTO.DTOout;

import java.math.BigDecimal;
import java.util.List;

public record CartItemsOut(List<ItemOut> items,
                           BigDecimal totalPrice){}

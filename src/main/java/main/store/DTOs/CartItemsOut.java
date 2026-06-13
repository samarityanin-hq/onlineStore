package DTOs;

import Entities.CartItem;

import java.util.List;

public record CartItemsOut(List<ItemOut> items,
                           double totalPrice){}

package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import main.store.DTOs.OrderItemOut;
import main.store.DTOs.OrderOut;
import main.store.Entities.*;
import main.store.Repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public OrderService(CartRepo cartRepo, UserRepo userRepo, ProductRepo productRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Transactional
    public OrderOut createOrder(CustomUserDetails userDetails) {
        User user = userRepo.findByEmail(userDetails.getUsername());
        List<CartItem> cartItems = cartRepo.findByUserId(userDetails.getId());
        BigDecimal orderCost = BigDecimal.ZERO;

        if (cartItems.isEmpty()){
            throw new EntityNotFoundException("cannot create order with empty cart");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems){
            Product product = productRepo.findProductByTitle(item.getItem().getTitle());
            if (product.getStorageQuantity() < item.getItemQuantity()){
                throw new IllegalArgumentException("not enough product in store. Remain:" +product.getStorageQuantity());
            }

            BigDecimal cartPositionCost = product.getPrice().multiply(BigDecimal.valueOf(item.getItemQuantity()));
            orderCost = orderCost.add(cartPositionCost);

            OrderItem orderItem = new OrderItem(user, product, item.getItemQuantity(), cartPositionCost);
            orderItems.add(orderItem);
            orderItemRepo.save(orderItem);

        }


        Order order = new Order(
                userRepo.getReferenceById(userDetails.getId()),
                Status.CREATED,
                orderCost
        );
        orderRepo.save(order);

        List<OrderItemOut> itemOuts = orderItems
                .stream()
                .map(this::convertToItemsOut)
                .toList();


        return new OrderOut(userDetails.getUsername(),
                userDetails.getRealName(),
                Status.CREATED,
                orderCost,
                itemOuts);
    }

    private OrderItemOut convertToItemsOut(OrderItem item){
        return new OrderItemOut(item.getItem().getTitle(),
                item.getItemQuantity(),
                item.getPriceAtPurchase());
    }
}

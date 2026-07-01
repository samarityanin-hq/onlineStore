package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import main.store.DTOs.*;
import main.store.Entities.*;
import main.store.Repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;


    @Transactional
    public OrderOut createOrder(CustomUserDetails userDetails) {
        List<CartItem> cartItems = cartRepo.findByUserId(userDetails.getId());
        BigDecimal orderCost = BigDecimal.ZERO;

        if (cartItems.isEmpty()){
            throw new EntityNotFoundException("cannot create order with empty cart");
        }

        User user = userRepo.getReferenceById(userDetails.getId());
        Order order = new Order(
                user,
                Status.CREATED,
                orderCost,
                cartItems.size()
        );

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems){
            OrderItem orderItem = createOrderItem(item, order);

            orderCost = orderCost.add(orderItem.getPositionCost());

            orderItems.add(orderItem);
        }
        order.setTotalPrice(orderCost);
        order.setTotalOrderItems(orderItems.size());
        saveOrderAndClearCart(orderItems, order);

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

    @Transactional
    public PaymentResponse pay(PaymentIn payment, Long orderId, CustomUserDetails userDetails) throws AccessDeniedException {
        Order order = orderRepo.findByUserIdAndOrderId(userDetails.getId(), orderId);

        if (order.getStatus().equals(Status.PAID)){
            throw new AccessDeniedException("order is already been paid");
        }
        if (payment.amount().compareTo(order.getTotalPrice()) < 0){
            throw new IllegalArgumentException("not enough money");
        }

        order.setStatus(Status.PAID);
        order.setPayDate(LocalDateTime.now());
        orderRepo.save(order);

        return new PaymentResponse(order.getId(),
                order.getUser().getEmail(),
                order.getTotalPrice(),
                order.getPayDate());
    }

    private OrderItemOut convertToItemsOut(OrderItem item){
        return new OrderItemOut(item.getItem().getTitle(),
                item.getItemQuantity(),
                item.getPriceAtPurchase());
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order){
        Product product = cartItem.getItem();
        if (product.getStorageQuantity() < cartItem.getItemQuantity()){
            throw new IllegalArgumentException("not enough product in store. Remain: %s"
                    .formatted(product.getStorageQuantity()));
        }
        product.setStorageQuantity(product.getStorageQuantity() - cartItem.getItemQuantity());

        return new OrderItem(order, product, cartItem.getItemQuantity());
    }

    private void saveOrderAndClearCart(List<OrderItem> items, Order order){
        orderRepo.save(order);
        orderItemRepo.saveAll(items);
        cartRepo.deleteAllByUser_Id(order.getUser().getId());
    }


    public List<SmallOrderOut> getOrders(CustomUserDetails userDetails) {
        return orderRepo.getOrdersByUserId(userDetails.getId());
    }
}

package main.store.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import main.store.CustomExceptions.EmptyCartException;
import main.store.CustomExceptions.InvalidPaymentAmountException;
import main.store.CustomExceptions.OrderAlreadyPaidException;
import main.store.CustomExceptions.ProductOutOfStockException;
import main.store.DTO.DTOin.PaymentIn;
import main.store.DTO.DTOout.FullOrderOut;
import main.store.DTO.DTOout.OrderItemOut;
import main.store.DTO.DTOout.OrderOut;
import main.store.DTO.DTOout.PaymentResponse;
import main.store.Entities.*;
import main.store.Enums.Status;
import main.store.Repositories.*;
import main.store.Security.CustomUserDetails;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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
    public FullOrderOut createOrder(CustomUserDetails userDetails) {
        List<CartItem> cartItems = cartRepo.findCartItemsByUserId(userDetails.getId());
        BigDecimal orderCost = BigDecimal.ZERO;

        if (cartItems.isEmpty()){
            throw new EmptyCartException(userDetails.getRealName(), userDetails.getUsername());
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


        return new FullOrderOut(userDetails.getUsername(),
                userDetails.getRealName(),
                Status.CREATED,
                orderCost,
                itemOuts);
    }

    @Transactional
    public PaymentResponse pay(PaymentIn payment, Long orderId, CustomUserDetails userDetails){
        Order order = orderRepo.findByUserIdAndOrderId(userDetails.getId(), orderId);

        if (order.getStatus().equals(Status.PAID)){
            throw new OrderAlreadyPaidException(orderId);
        }
        if (payment.amount().compareTo(order.getTotalPrice()) != 0){
            throw new InvalidPaymentAmountException(orderId, order.getTotalPrice(), payment.amount());
        }

        order.setStatus(Status.PAID);
        order.setPayDate(LocalDateTime.now());
        orderRepo.save(order);

        return new PaymentResponse(order.getId(),
                userDetails.getUsername(),
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
            throw new ProductOutOfStockException(product.getTitle(), product.getId());
        }
        product.setStorageQuantity(product.getStorageQuantity() - cartItem.getItemQuantity());

        return new OrderItem(order, product, cartItem.getItemQuantity());
    }

    private void saveOrderAndClearCart(List<OrderItem> items, Order order){
        orderRepo.save(order);
        orderItemRepo.saveAll(items);
        cartRepo.deleteAllByUser_Id(order.getUser().getId());
    }


    public List<OrderOut> getOrders(CustomUserDetails userDetails) {
        return orderRepo.getOrdersByUserId(userDetails.getId());
    }
}

package main.store.Repositories;

import main.store.DTO.Response.OrderOut;
import main.store.Entities.Order;
import main.store.Entities.OrderItem;
import main.store.Enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user.id =:userId")
    Optional<Order> findByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);

    @Query("""
    SELECT new main.store.DTO.Response.OrderOut(
        o.status, o.totalPrice, o.totalOrderItems, o.dateTime, o.payDate)
    FROM Order o
    WHERE o.user.id = :userId
    """)
    List<OrderOut> getOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.expiresAt <= :now")
    List<Order> findExpiredOrders(@Param("status")Status status, @Param("now")LocalDateTime now);

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.item WHERE oi.order.id =:orderId")
    List<OrderItem> findOrderItemsById(@Param("orderId") Long orderId);
}

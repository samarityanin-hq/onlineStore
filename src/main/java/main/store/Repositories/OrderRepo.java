package main.store.Repositories;

import main.store.DTOs.SmallOrderOut;
import main.store.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user.id =: userId")
    Order findByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);


    @Query("""
    SELECT new main.store.DTOs.SmallOrderOut(
        o.status, o.totalPrice, o.totalOrderItems, o.dateTime, o.payDate)
    FROM Order o
    WHERE o.user.id = :userId
    """)
    List<SmallOrderOut> getOrdersByUserId(@Param("userId") Long userId);
}

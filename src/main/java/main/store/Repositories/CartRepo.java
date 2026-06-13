package main.store.Repositories;

import main.store.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CartRepo extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(long id);
    @Query("SELECT SUM(c.item.price * c.itemQuantity) FROM CartItem c WHERE c.user.id = :userId")
    BigDecimal calculateCartCost(@Param("userId") Long userId);

}

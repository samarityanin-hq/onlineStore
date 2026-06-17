package main.store.Repositories;

import main.store.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CartRepo extends JpaRepository<CartItem, Long> {

    @Query("SELECT c FROM CartItem c JOIN FETCH c.item WHERE c.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    CartItem findCartItemByUser_IdAndItem_Id(Long userId, Long itemId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
    void deleteAllByUser_Id(@Param("userId") Long userId);

    /*@Query("SELECT SUM(c.item.price * c.itemQuantity) FROM CartItem c WHERE c.user.id = :userId")
    BigDecimal calculateCartCost(@Param("userId") Long userId);*/

}

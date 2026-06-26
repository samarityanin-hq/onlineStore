package main.store.Repositories;

import main.store.DTOs.CartItemsOut;
import main.store.DTOs.ItemOut;
import main.store.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<CartItem, Long> {

    @Query("""
        SELECT new main.store.DTOs.ItemOut(i.item.title, i.itemQuantity,i.positionCost)
        FROM CartItem i
        WHERE i.user.id = :userId
    """)
    List<ItemOut> findDTO(@Param("userId") Long userId);

    @Query("SELECT c FROM CartItem c JOIN FETCH c.item WHERE c.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    CartItem findCartItemByUser_IdAndItem_Title(Long userId, String itemTitle);

    void deleteAllByUser_Id(Long userId);


}

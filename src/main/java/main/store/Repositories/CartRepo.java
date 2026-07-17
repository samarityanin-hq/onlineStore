package main.store.Repositories;

import jakarta.transaction.Transactional;
import main.store.DTO.DTOout.ItemOut;
import main.store.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepo extends JpaRepository<CartItem, Long> {

    @Query("""
        SELECT new main.store.DTO.DTOout.ItemOut(i.item.title, i.itemQuantity,i.positionCost)
        FROM CartItem i
        WHERE i.user.id = :userId
    """)
    List<ItemOut> findDTO(@Param("userId") Long userId);

    @Query("SELECT c FROM CartItem c JOIN FETCH c.item WHERE c.user.id = :userId")
    List<CartItem> findCartItemsByUserId(@Param("userId") Long userId);

    CartItem getByItem_IdAndUser_Id(Long itemId, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.id = : itemdId AND c.user.id = :userId")
    void deleteByIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId);
    
    void deleteAllByUser_Id(Long userId);


}

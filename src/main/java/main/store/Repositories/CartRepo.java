package main.store.Repositories;

import main.store.DTO.Response.ItemOut;
import main.store.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<CartItem, Long> {

    @Query("""
        SELECT new main.store.DTO.Response.ItemOut(i.item.title, i.itemQuantity,i.positionCost)
        FROM CartItem i
        WHERE i.user.id = :userId
    """)
    List<ItemOut> findDTO(@Param("userId") Long userId);

    @Query("""
        SELECT c FROM CartItem c 
        JOIN FETCH c.item
        WHERE c.user.id = :userId
        ORDER BY c.item.id
    """)
    List<CartItem> findCartItemsByUserId(@Param("userId") Long userId);

    Optional<CartItem> getByItem_IdAndUser_Id(Long itemId, Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM CartItem c WHERE c.id = :itemId AND c.user.id = :userId")
    int deleteByIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId);
    
    void deleteAllByUser_Id(Long userId);


}

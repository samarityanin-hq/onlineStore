package Repositories;

import Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(long id);
}

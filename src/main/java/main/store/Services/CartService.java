package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import main.store.DTOs.CartItemsOut;
import main.store.DTOs.ItemOut;
import main.store.Entities.CartItem;
import main.store.Entities.CustomUserDetails;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.ProductRepo;
import main.store.Repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    @Transactional
    public void addToCart(long productId, CustomUserDetails userDetails) {
        Product product = productRepo.getReferenceById(productId);
        User user = userRepo.getReferenceById(userDetails.getId());

        CartItem cartItem = cartRepo.getByItem_IdAndUser_Id(productId, userDetails.getId());

        if (cartItem == null){
            cartItem = new CartItem(user, product, 1);
            cartRepo.save(cartItem);
        }
        else {
            cartItem.setItemQuantity(cartItem.getItemQuantity()+1);
        }
    }


    public CartItemsOut showCartItems(CustomUserDetails userDetails) {
        return cartItemsOut(userDetails);
    }

    @Transactional
    public CartItemsOut clear(CustomUserDetails userDetails) {
        cartRepo.deleteAllByUser_Id(userDetails.getId());
        return cartItemsOut(userDetails);
    }

    @Transactional
    public CartItemsOut decrementCartPosition(long productId, CustomUserDetails userDetails) {

        CartItem cartItem = cartRepo.getByItem_IdAndUser_Id(productId, userDetails.getId());

        if (cartItem.getItemQuantity() > 1){
            cartItem.setItemQuantity(cartItem.getItemQuantity()-1);
        }
        else {
            cartRepo.delete(cartItem);
        }

        return cartItemsOut(userDetails);
    }

    @Transactional
    public CartItemsOut deleteCartPosition(long itemId, CustomUserDetails userDetails){

        cartRepo.deleteByIdAndUserId(itemId, userDetails.getId());

        return cartItemsOut(userDetails);
    }

    private CartItemsOut cartItemsOut(CustomUserDetails userDetails){

        List<ItemOut> items = cartRepo.findDTO(userDetails.getId());

        BigDecimal cartCost = items.stream()
                .map(ItemOut :: totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartItemsOut(items, cartCost);
    }

}

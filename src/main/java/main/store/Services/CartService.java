package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import main.store.DTO.Response.CartItemsOut;
import main.store.DTO.Response.ItemOut;
import main.store.Entities.CartItem;
import main.store.Security.CustomUserDetails;
import main.store.Repositories.CartRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepo cartRepo;

    @Transactional
    public void addToCart(long productId, CustomUserDetails userDetails) {
        try {
            cartRepo.upsertCartItem(userDetails.getId(), productId);
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException(String.format("Product with id: %d not found", productId));
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

        CartItem cartItem = cartRepo.getByItem_IdAndUser_Id(productId, userDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("cart item doesnt exist"));

        if (cartItem.getItemQuantity() > 1){
            cartItem.setItemQuantity(cartItem.getItemQuantity()-1);
            cartRepo.save(cartItem);
        }
        else {
            cartRepo.delete(cartItem);
            cartRepo.flush();
        }

        return cartItemsOut(userDetails);
    }

    @Transactional
    public CartItemsOut deleteCartPosition(long itemId, CustomUserDetails userDetails){

        if (cartRepo.deleteByIdAndUserId(itemId, userDetails.getId()) == 0) {
            throw new EntityNotFoundException("INVALID ID: item doesnt exist");
        }

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

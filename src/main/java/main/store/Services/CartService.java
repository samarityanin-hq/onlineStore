package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import main.store.DTOs.CartItemsOut;
import main.store.DTOs.ItemOut;
import main.store.Entities.CartItem;
import main.store.Entities.CustomUserDetails;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.ProductRepo;
import main.store.Repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public CartService(CartRepo cartRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public void addToCart(String productTitle, CustomUserDetails userDetails) {

        Product product = productRepo.findProductByTitle(productTitle);
        if (product == null){
            throw new EntityNotFoundException("Product not found");
        }

        User user = userRepo.findByEmail(userDetails.getUsername());
        CartItem cartItem = cartRepo.findCartItemByUser_IdAndItem_Title(user.getId(), productTitle)
                .orElseThrow(()-> new EntityNotFoundException("No such product in your cart"));

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
    public CartItemsOut decrementCartPosition(String productTitle, CustomUserDetails userDetails) {

        CartItem cartItem = findCartItem(productTitle, userDetails);

        if (cartItem.getItemQuantity() > 1){
            cartItem.setItemQuantity(cartItem.getItemQuantity()-1);
        }
        else {
            cartRepo.delete(cartItem);
        }

        return cartItemsOut(userDetails);
    }

    @Transactional
    public CartItemsOut deleteCartPosition(String productTitle, CustomUserDetails userDetails){

        cartRepo.delete(findCartItem(productTitle, userDetails));

        return cartItemsOut(userDetails);
    }

    private CartItemsOut cartItemsOut(CustomUserDetails userDetails){

        List<ItemOut> items = cartRepo.findDTO(userDetails.getId());

        BigDecimal cartCost = items.stream()
                .map(ItemOut :: totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartItemsOut(items, cartCost);
    }

    private CartItem findCartItem(String productTitle, CustomUserDetails userDetails){

        CartItem cartItem = cartRepo.findCartItemByUser_IdAndItem_Title(userDetails.getId(), productTitle)
                .orElseThrow(()-> new EntityNotFoundException("No such product in your cart"));;

        return cartItem;
    }

}

package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import main.store.DTOs.CartItemsOut;
import main.store.DTOs.ItemOut;
import main.store.Entities.CartItem;
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

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public CartService(CartRepo cartRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Transactional
    public void addToCart(String productTitle, Principal principal) {
        String email = principal.getName();

        Product product = productRepo.findProductByTitle(productTitle);
        if (product == null){
            throw new EntityNotFoundException("Product not found");
        }

        User user = userRepo.findByEmail(email);
        CartItem cartItem = cartRepo.findCartItemByUser_IdAndItem_Id(user.getId(), product.getId());
        if (cartItem == null){
            cartItem = new CartItem(user, product, 1);
            cartRepo.save(cartItem);
        }
        else {
            cartItem.setItemQuantity(cartItem.getItemQuantity()+1);

        }

    }


    public CartItemsOut showCartItems(Principal principal) {
        return cartItemsOut(principal.getName());
    }

    @Transactional
    public CartItemsOut clear(Principal principal) {
        cartRepo.deleteAllByUser_Id(userRepo.findByEmail(principal.getName()).getId());
        return cartItemsOut(principal.getName());
    }

    @Transactional
    public CartItemsOut decrementCartPosition(String productTitle, Principal principal) {

        CartItem cartItem = findCartItem(productTitle, principal);

        if (cartItem.getItemQuantity() > 1){
            cartItem.setItemQuantity(cartItem.getItemQuantity()-1);
        }
        else {
            cartRepo.delete(cartItem);
        }

        return cartItemsOut(principal.getName());
    }

    @Transactional
    public CartItemsOut deleteCartPosition(String productTitle, Principal principal){

        cartRepo.delete(findCartItem(productTitle, principal));

        return cartItemsOut(principal.getName());
    }

    private CartItemsOut cartItemsOut(String email){
        User user = userRepo.findByEmail(email);

        List<CartItem> items = cartRepo.findByUserId(user.getId());

        BigDecimal cartCost = items.stream()
                .map(CartItem :: getPositionCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartItemsOut(items
                .stream()
                .map(this::convertToItemOut)
                .toList(), cartCost);
    }

    private CartItem findCartItem(String productTitle, Principal principal){
        User user = userRepo.findByEmail(principal.getName());

        Product product = productRepo.findProductByTitle(productTitle);
        if (product == null){
            throw new EntityNotFoundException("cannot find product to delete");
        }

        CartItem cartItem = cartRepo.findCartItemByUser_IdAndItem_Id(user.getId(), product.getId());

        if (cartItem == null){
            throw new EntityNotFoundException("this product not in your cart");
        }

        return cartItem;
    }


    private ItemOut convertToItemOut(CartItem item){
        return new ItemOut(item.getItem().getTitle(),
                item.getItemQuantity(),
                item.getPositionCost());
    }

}

package main.store.Services;

import main.store.DTOs.CartItemsOut;
import main.store.DTOs.ItemOut;
import main.store.Entities.CartItem;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.ProductRepo;
import main.store.Repositories.UserRepo;
import org.springframework.stereotype.Service;

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

    public void addToCart(String productTitle, Principal principal) {
        String email = principal.getName();

        Product product = productRepo.findProductByTitle(productTitle);
        User user = userRepo.findUserByEmail(email);
        CartItem cartItem = new CartItem(user, product, 1);

        cartRepo.save(cartItem);

    }


    public CartItemsOut showCartItems(Principal principal) {
        String email = principal.getName();

        User user = userRepo.findUserByEmail(email);
        List<CartItem> items = cartRepo.findByUserId(user.getId());


        return new CartItemsOut(items
                .stream()
                .map(this::convertToItemOut)
                .toList(), cartRepo.calculateCartCost(user.getId()));
    }


    private ItemOut convertToItemOut(CartItem item){
        return new ItemOut(item.getItem().getTitle(),
                item.getItemQuantity(),
                item.getPositionCost());
    }



}

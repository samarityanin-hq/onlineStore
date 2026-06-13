package main.store.Controllers;

import main.store.DTOs.CartItemsOut;
import main.store.Services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCartItem(
            @RequestParam String productTitle,
            Principal principal
    ){
        cartService.addToCart(productTitle, principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/showCartItems")
    public ResponseEntity<CartItemsOut> showCartItems(Principal principal){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.showCartItems(principal));

    }}

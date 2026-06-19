package main.store.Controllers;

import main.store.DTOs.CartItemsOut;
import main.store.Services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController

@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping("/add")
    public ResponseEntity<Void> addCartItem(
            @RequestParam String productTitle,
            Principal principal
    ) {
        cartService.addToCart(productTitle, principal);
        log.info("called method addCartItem");
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/showCartItems")
    public ResponseEntity<CartItemsOut> showCartItems(Principal principal) {

        log.info("called method showCartItems");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.showCartItems(principal));

    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartItemsOut> clearCart(Principal principal) {
        log.info("called method clearCart");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.clear(principal));
    }

    @DeleteMapping("/decrement")
    public ResponseEntity<CartItemsOut> decrementCartPosition(
            @RequestParam String productTitle,
            Principal principal
    ){
        log.info("called method decrementCartPosition");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.decrementCartPosition(productTitle, principal));
    }

    @DeleteMapping("/deletePosition")
    public ResponseEntity<CartItemsOut> deleteCartPosition(
            @RequestParam String productTitle,
            Principal principal
    ){
        log.info("called method deleteCartPosition");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.deleteCartPosition(productTitle, principal));
    }

}
package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOout.CartItemsOut;
import main.store.Security.CustomUserDetails;
import main.store.Services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Контроллер корзины", description = "Управление корзиной (только для аутентифицированных юзеров)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Operation(summary = "Добавить товар в корзину (требуется id товара)")
    @PostMapping("/add")
    public ResponseEntity<Void> addCartItem(
            @RequestParam long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.addToCart(productId, userDetails);
        log.info("called method addCartItem");
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Показать корзину")
    @GetMapping("/showCartItems")
    public ResponseEntity<CartItemsOut> showCartItems(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        log.info("called method showCartItems");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.showCartItems(userDetails));

    }

    @Operation(summary = "Очистить корзину")
    @DeleteMapping("/clear")
    public ResponseEntity<CartItemsOut> clearCart(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        log.info("called method clearCart");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.clear(userDetails));
    }

    @Operation(summary = "Уменьшить позицию корзины на 1")
    @DeleteMapping("/decrement")
    public ResponseEntity<CartItemsOut> decrementCartPosition(
            @RequestParam long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        log.info("called method decrementCartPosition");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.decrementCartPosition(productId, userDetails));
    }

    @Operation(summary = "Удалить позицию корзины")
    @DeleteMapping("/deletePosition")
    public ResponseEntity<CartItemsOut> deleteCartPosition(
            @RequestParam long itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        log.info("called method deleteCartPosition");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.deleteCartPosition(itemId, userDetails));
    }

}
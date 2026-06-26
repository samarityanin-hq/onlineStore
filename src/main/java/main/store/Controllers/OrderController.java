package main.store.Controllers;

import jakarta.validation.Valid;
import main.store.DTOs.OrderOut;
import main.store.DTOs.PaymentIn;
import main.store.DTOs.PaymentResponse;
import main.store.DTOs.SmallOrderOut;
import main.store.Entities.CustomUserDetails;
import main.store.Services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController{
    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/createOrder")
    public ResponseEntity<OrderOut> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        log.info("called method createOrder");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(userDetails));

    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentResponse> pay(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PaymentIn payment,
            @PathVariable long orderId) throws AccessDeniedException {
        log.info("called method pay");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.pay(payment, orderId,userDetails));
    }

    @GetMapping("/orderList")
    public ResponseEntity<List<SmallOrderOut>> getOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        log.info("called method getOrders");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrders(userDetails));
    }
}
